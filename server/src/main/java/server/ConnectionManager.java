package server;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
    public void leaveGame(String authToken, Integer gameID){
        gamesToUsers.get(gameID).remove(authToken);
        //remove from allUsers??
    }
    public void notifyUser(String authToken, LoadGameMessage message) throws IOException {
        Connection user = allUsers.get(authToken);
        user.send(message.toString()); //not sure what will happen with this toString...
    }
    public void notifyAllButUser(String authToExclude, Integer gameID, NotificationMessage notification) throws IOException {
        ArrayList<String> usersInGame = gamesToUsers.get(gameID);
        Connection connection;
        for (String user : usersInGame){
            if (Objects.equals(authToExclude, user)){continue;}
            connection = allUsers.get(user);
            connection.send(notification.getMessage());
        }
    }
    public void notifyAllInGame(Integer gameID, ServerMessage notification) throws IOException {
        ArrayList<String> usersInGame = gamesToUsers.get(gameID);
        Connection connection;
        for (String user : usersInGame){//probs need to add some kind of "if message of type LoadGame"
            connection = allUsers.get(user);
            connection.send(notification.toString()); //will need to look at toString fr
        }
    }
}
