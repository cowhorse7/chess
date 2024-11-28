package server;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    public HashMap<Integer, ArrayList<UserData>> gamesWithUsers = new HashMap<>();
}
