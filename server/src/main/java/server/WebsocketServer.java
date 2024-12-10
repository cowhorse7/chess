package server;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebsocketServer {
    private final ConnectionManager manager = new ConnectionManager();
    private String message = "";
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;
    private AuthData user;
    public WebsocketServer(AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }
    private String playerColor(GameData gameData){
        String color = "";
        if (Objects.equals(gameData.whiteUsername(), user.username())){
            color = "white";
        }
        else if (Objects.equals(gameData.blackUsername(), user.username())){
            color = "black";
        }
        return color;
    }
    @OnWebSocketMessage
        public void onMessage(Session session, String message) throws Exception {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            user = authDataAccess.getAuthByToken(command.getAuthToken());
            Integer gameID = command.getGameID();
             switch (command.getCommandType()) {
                    case LEAVE -> leave(command.getAuthToken(), gameID);
                    case RESIGN -> resign(command.getAuthToken(), gameID);
                    case CONNECT -> connect(command.getAuthToken(), session, gameID);
                    case MAKE_MOVE -> {
                        MakeMoveCommand mmCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                        makeMove(command.getAuthToken(), session, gameID, mmCommand.getMove());
                    }
                }

        }
        private void leave(String authToken, Integer gameID) throws Exception {
            GameData gameData = gameDataAccess.getGame(gameID);
            String color = playerColor(gameData);

            if (Objects.equals(color, "white")){
                gameData = new GameData(gameID, null, gameData.blackUsername(), gameData.gameName(), gameData.game());
                gameDataAccess.updateGame(gameID, gameData);
            }
            else if (Objects.equals(color, "black")){
                gameData = new GameData(gameID, gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
                gameDataAccess.updateGame(gameID, gameData);
            }
            message = String.format("%s has left the game.\n", user.username());
            ServerMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            manager.notifyAllButUser(authToken, gameID, toOthers);

            manager.leaveGame(authToken, gameID);
        }
        private void resign(String authToken, Integer gameID) throws Exception {
            GameData gameData = gameDataAccess.getGame(gameID);
            String color = playerColor(gameData);
            if (manager.gameEnded(gameID)){
                message = "the game has already ended\n";
                ServerMessage toUser = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
                manager.notifyUser(authToken, toUser);
            }
            else if (Objects.equals(color, "white") || Objects.equals(color, "black")){
                message = String.format("%s has resigned.\nThe game is now ended.\n", user.username());
                ServerMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                manager.notifyAllButUser(null, gameID, toOthers);
                manager.endGame(gameID);
                gameDataAccess.removeGame(gameID);
            }
            else{
                message = "observers cannot resign.\n";
                ServerMessage toUser = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
                manager.notifyUser(authToken, toUser);
            }
        }
        private void connect(String authToken, Session session, Integer gameID) throws Exception {
            GameData gameData = gameDataAccess.getGame(gameID);
            if(gameData == null) {
                message = "game could not be found.\n";
                ServerMessage toUser = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
                String msg = new Gson().toJson(toUser);
                session.getRemote().sendString(msg);
                return;
            }
            if(!checkAuth(session)){return;}
            String color = playerColor(gameData);
            ChessGame game = gameData.game();
            manager.add(authToken, session, gameID);
            if (Objects.equals(color, "white")){
                message = String.format("%s has joined %s as %s.\n", user.username(), gameData.gameName(), color);
            }
            else if (Objects.equals(color, "black")){
                message = String.format("%s has joined %s as %s.\n", user.username(), gameData.gameName(), color);
            }
            else{message = String.format("%s has joined %s as observer.\n", user.username(), gameData.gameName());}
            ServerMessage onJoin = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            manager.notifyUser(authToken, onJoin);
            ServerMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            manager.notifyAllButUser(authToken, gameID, toOthers);
        }
        private void makeMove(String authToken, Session session, Integer gameID, ChessMove move) throws Exception {
            if(!checkAuth(session)){return;}
            GameData gameData = gameDataAccess.getGame(gameID);
            String color = playerColor(gameData);
            ChessGame game = gameData.game();
            ChessGame.TeamColor teamColor = game.getTeamTurn();
            if(manager.gameEnded(gameID)){
                message = "you cannot play after the game has ended\n";
                ServerMessage toUser = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
                manager.notifyUser(authToken, toUser);
            }
            else if(!color.equals("white") && !color.equals("black")){
                message = "observers cannot play\n";
                ServerMessage toUser = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
                manager.notifyUser(authToken, toUser);
            }
            else if((color.equals("white") && !teamColor.equals(ChessGame.TeamColor.WHITE)) ||
                    (color.equals("black") && !teamColor.equals(ChessGame.TeamColor.BLACK))){
                message = "you cannot move when it is not your turn\n";
                ServerMessage toUser = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
                manager.notifyUser(authToken, toUser);
            }
            else {
                ChessPosition start = move.getStartPosition();
                ChessPosition end = move.getEndPosition();
                char[] columns = {' ','a','b','c','d','e','f','g','h'};
                try{
                game.makeMove(move);
                }catch (Exception ex){
                    message = "invalid move\n";
                    ServerMessage toUser = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
                    manager.notifyUser(authToken, toUser);
                    return;
                }
                gameData = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
                gameDataAccess.updateGame(gameID, gameData);

                ServerMessage onMove = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
                manager.notifyAllButUser(null, gameID, onMove);

                message = String.format("%s moved piece at %d,%s to %d,%s.\n",
                        user.username(), start.getRow(), columns[start.getColumn()], end.getRow(), columns[end.getColumn()]);
                ServerMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                manager.notifyAllButUser(authToken, gameID, toOthers);

                checkGameConditions(gameID, game, color);
            }
        }
            private void checkGameConditions(Integer gameID, ChessGame game, String color) throws Exception {
                ServerMessage toOthers;
                String username = "";
                GameData gameData = gameDataAccess.getGame(gameID);
                if(color.equals("white")){
                    color = "black";
                    username = gameData.blackUsername();
                }
                else if(color.equals("black")){
                    color = "white";
                    username = gameData.whiteUsername();
                }
                ChessGame.TeamColor teamColor = game.getTeamTurn();
                if (game.isInCheckmate(teamColor)) {
                    message = String.format("%s (%s) is in checkmate\n", username, color);
                    toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                    manager.notifyAllButUser(null, gameID, toOthers);
                    manager.endGame(gameID);
                } else if (game.isInCheck(teamColor)) {
                    message = String.format("%s (%s) is in check\n", username, color);
                    toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                    manager.notifyAllButUser(null, gameID, toOthers);
                } else if (game.isInStalemate(teamColor)) {
                    message = String.format("%s is in stalemate\n", gameData.gameName());
                    toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                    manager.notifyAllButUser(null, gameID, toOthers);
                    manager.endGame(gameID);
                }
        }
        private boolean checkAuth(Session session) throws IOException {
            if(user == null) {
                message = "not authorized.\n";
                ServerMessage toUser = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
                String msg = new Gson().toJson(toUser);
                session.getRemote().sendString(msg);
                return false;
            }
            return true;
        }
}
