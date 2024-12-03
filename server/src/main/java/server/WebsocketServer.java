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
//        public static void main(String[] args) {
//            Spark.port(8080);
//            Spark.webSocket("/ws", WSServer.class);
//            Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//        }
    private ConnectionManager manager = new ConnectionManager();
    private String message = "";
    private AuthDAO authDataAccess;
    private GameDAO gameDataAccess;
    private AuthData user;
    public WebsocketServer(AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }
        @OnWebSocketMessage
        public void onMessage(Session session, String message) throws Exception {
            //session.getRemote().sendString("WebSocket response: " + message);
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            user = authDataAccess.getAuthByToken(command.getAuthToken());
            switch (command.getCommandType()){
                case LEAVE -> leave(command.getAuthToken(), command.getGameID());
                case RESIGN -> resign(command.getAuthToken(), command.getGameID());
                case CONNECT -> connect(command.getAuthToken(), session, command.getGameID());
                case MAKE_MOVE -> {
                    MakeMoveCommand mmCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMove(command.getAuthToken(), command.getGameID(), mmCommand.getMove());}
            }
        }
        private void leave(String authToken, Integer gameID) throws Exception {
            manager.leaveGame(authToken, gameID);
            message = String.format("%s has left the game.\n", user.username());
            ServerMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            manager.notifyAllButUser(authToken, gameID, toOthers);
        }
        private void resign(String authToken, Integer gameID) throws Exception {
            leave(authToken,gameID);
            message = String.format("%s has resigned.\n", user.username());
            ServerMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            manager.notifyAllInGame(gameID, toOthers);
        }
        private void connect(String authToken, Session session, Integer gameID) throws Exception {
            GameData gameData = gameDataAccess.getGame(gameID);
            String color = "";
            ChessGame game = gameData.game();
            manager.add(authToken, session, gameID);
            if (Objects.equals(gameData.whiteUsername(), user.username())){
                color = "white";
                message = String.format("%s has joined %s as %s.\n", user.username(), gameData.gameName(), color);
            }
            else if (Objects.equals(gameData.blackUsername(), user.username())){
                color = "black";
                message = String.format("%s has joined %s as %s.\n", user.username(), gameData.gameName(), color);
            }
            else{message = String.format("%s has joined %s as observer.\n", user.username(), gameData.gameName());}
            ServerMessage onJoin = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            manager.notifyUser(authToken, onJoin);
            ServerMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            manager.notifyAllButUser(authToken, gameID, toOthers);
        }
        private void makeMove(String authToken, Integer gameID, ChessMove move) throws Exception {
            GameData gameData = gameDataAccess.getGame(gameID);
            ChessGame game = gameData.game();
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();
            game.makeMove(move);
            ServerMessage onMove = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            manager.notifyAllInGame(gameID, onMove);
            message = String.format("%s moved piece at %d,%d to %d,%d.\n",
                    user.username(), start.getRow(), start.getColumn(), end.getRow(), end.getColumn());
            ServerMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            manager.notifyAllButUser(authToken, gameID, toOthers);
            String color = String.valueOf(game.getTeamTurn());
            if (game.isInCheckmate(game.getTeamTurn())){
                message = String.format("%s is in checkmate", color);
                toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                manager.notifyAllInGame(gameID, toOthers);
            }
            else if (game.isInCheck(game.getTeamTurn())){
                message = String.format("%s is in check", color);
                toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                manager.notifyAllInGame(gameID, toOthers);
            }
            else if (game.isInStalemate(game.getTeamTurn())){
                message = String.format("%s is in stalemate", gameData.gameName());
                toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                manager.notifyAllInGame(gameID, toOthers);
            }
        }
        @OnWebSocketError
    public void errorTime(Throwable ex) throws IOException {
        ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
        manager.notifyUser(user.authToken(), errorMessage);
    }
}
