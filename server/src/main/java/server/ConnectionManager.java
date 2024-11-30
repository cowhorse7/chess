package server;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {
    public HashMap<Integer, ArrayList<String>> gamesToUsers = new HashMap<>();
    public HashMap<String, Connection> allUsers = new HashMap<>();

    public void add(String authToken, Session session, Integer gameID){
        Connection currentUser = new Connection(authToken, session);
        allUsers.put(authToken, currentUser);
        ArrayList<String> players = new ArrayList<>();
        players.add(authToken);
        gamesToUsers.put(gameID, players);
    }
    public void remove(String authToken, Integer gameID){
        gamesToUsers.get(gameID).remove(authToken);
    }
    public void notifyUser(String authToken, ServerMessage message) throws IOException {
        Connection user = allUsers.get(authToken);
        user.send(message.toString()); //not sure what will happen with this toString...
    }
    public void notifyAllButUser(String authToExclude, Integer gameID, ServerMessage notification) throws IOException {

    }
    public void notifyAllInGame(String authToExclude, Integer gameID, ServerMessage notification) throws IOException {

    }
}
