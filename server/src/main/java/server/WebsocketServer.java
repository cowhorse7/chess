package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebsocketServer {
//        public static void main(String[] args) {
//            Spark.port(8080);
//            Spark.webSocket("/ws", WSServer.class);
//            Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//        }

        @OnWebSocketMessage
        public void onMessage(Session session, String message) throws Exception {
            session.getRemote().sendString("WebSocket response: " + message);
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            switch (command.getCommandType()){
                case LEAVE -> leave();
                case RESIGN -> resign();
                case CONNECT -> connect();
                case MAKE_MOVE -> makeMove();
            }
        }
        private void leave(){

        }
        private void resign(){}
        private void connect(){}
        private void makeMove(){}
}
