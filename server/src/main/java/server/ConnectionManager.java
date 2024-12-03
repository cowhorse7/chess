package server;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
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
    private final Gson serializer = new Gson();

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
    public void notifyUser(String authToken, ServerMessage message) throws IOException {
        Connection userConn = allUsers.get(authToken);
        String note = serializer.toJson(message);
        userConn.send(note);
    }
    public void notifyAllButUser(String authToExclude, Integer gameID, ServerMessage notification) throws IOException {
        ArrayList<String> usersInGame = gamesToUsers.get(gameID);
        String note = serializer.toJson(notification);
        Connection connection;
        for (String user : usersInGame){
            if (Objects.equals(authToExclude, user)){continue;}
            connection = allUsers.get(user);
            connection.send(note);
        }
    }
    public void notifyAllInGame(Integer gameID, ServerMessage notification) throws IOException {
        ArrayList<String> usersInGame = gamesToUsers.get(gameID);
        String note = serializer.toJson(notification);
        Connection connection;
        for (String user : usersInGame){
            connection = allUsers.get(user);
            connection.send(note);
        }
    }
}
