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
    public HashMap<String, Connection> resignedPlayers = new HashMap<>();
    private final Gson serializer = new Gson();

    public void add(String authToken, Session session, Integer gameID){
        if(gamesToUsers.get(gameID)==null){
            ArrayList<String> players = new ArrayList<>();
            gamesToUsers.put(gameID, players);
        }
        Connection currentUser = new Connection(authToken, session);
        allUsers.put(authToken, currentUser);
        ArrayList<String> players=gamesToUsers.get(gameID);
        players.add(authToken);
    }
    public void leaveGame(String authToken, Integer gameID){
        gamesToUsers.get(gameID).remove(authToken);
        allUsers.remove(authToken);
        resignedPlayers.remove(authToken);
    }
    public void resign(String authToken){
        Connection currentUser = allUsers.get(authToken);
        resignedPlayers.put(authToken, currentUser);
    }
    public boolean userResigned(String authToken){
        return resignedPlayers.get(authToken) != null;
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
}
