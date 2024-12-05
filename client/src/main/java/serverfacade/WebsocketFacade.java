package serverfacade;
import com.google.gson.*;
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
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
    public void leaveGame(){}

}
