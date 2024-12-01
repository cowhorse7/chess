package server;

import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebsocketServer {
//        public static void main(String[] args) {
//            Spark.port(8080);
//            Spark.webSocket("/ws", WSServer.class);
//            Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//        }
    ConnectionManager manager = new ConnectionManager();
    String message = "";

        @OnWebSocketMessage
        public void onMessage(Session session, String message) throws Exception {
            session.getRemote().sendString("WebSocket response: " + message);
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            switch (command.getCommandType()){
                case LEAVE -> leave(command.getAuthToken(), command.getGameID());
                case RESIGN -> resign(command.getAuthToken(), command.getGameID());
                case CONNECT -> connect(command.getAuthToken(), session, command.getGameID());
                case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID());
            }
        }
        private void leave(String authToken, Integer gameID) throws IOException {
            manager.leaveGame(authToken, gameID);
            message = String.format("player has left game %d.\n", gameID);
            NotificationMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            manager.notifyAllButUser(authToken, gameID, toOthers);
        }
        private void resign(String authToken, Integer gameID) throws IOException {
            leave(authToken,gameID);
            message = "player has resigned.\n";
            NotificationMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            manager.notifyAllInGame(gameID, toOthers);
        }
        private void connect(String authToken, Session session, Integer gameID) throws IOException {
            manager.add(authToken, session, gameID);
            message = String.format("player has joined game %d.\n", gameID);//FIXME: make message to include player/observer's name
            LoadGameMessage onJoin = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameID);
            manager.notifyUser(authToken, onJoin);
            NotificationMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            manager.notifyAllButUser(authToken, gameID, toOthers);
        }
        private void makeMove(String authToken, Integer gameID) throws IOException {//, ChessMove move){
            LoadGameMessage onMove = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameID);
            manager.notifyAllInGame(gameID, onMove);
            message = "player has moved piece at x to y.\n";//move.getStartPosition, move.getEndPosition
            NotificationMessage toOthers = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            manager.notifyAllButUser(authToken, gameID, toOthers);
        }
}