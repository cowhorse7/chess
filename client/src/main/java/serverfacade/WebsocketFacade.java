package serverfacade;
import com.google.gson.*;
import websocket.messages.*;
import java.net.URI;
import javax.websocket.*;

public class WebsocketFacade extends Endpoint{
    Session session;
    NotificationHandler notificationHandler;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
