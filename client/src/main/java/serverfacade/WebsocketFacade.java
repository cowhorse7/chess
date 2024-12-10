package serverfacade;
import chess.ChessMove;
import com.google.gson.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;
import java.net.URI;
import javax.websocket.*;

public class WebsocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;
    public WebsocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    switch (notification.getServerMessageType()) {
                        case ERROR:
                            notification = new Gson().fromJson(message, ErrorMessage.class);
                            break;
                        case NOTIFICATION:
                            notification = new Gson().fromJson(message, NotificationMessage.class);
                            break;
                        case LOAD_GAME:
                            notification = new Gson().fromJson(message, LoadGameMessage.class);
                            break;
                    }
                    notificationHandler.notify(notification);
                }
            });
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void enterGame(String authToken, int gameID) throws Exception {
        try {
            UserGameCommand action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
    public void leaveGame(String authToken, int gameID) throws Exception{
        try{
            UserGameCommand action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
    public void resignGame(String authToken, int gameID) throws Exception {
        try {
            UserGameCommand action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception {
        try {
            UserGameCommand action = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
