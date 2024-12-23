package server;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ConnectionManager {
    private HashMap<Integer, ArrayList<String>> gamesToUsers = new HashMap<>();
    private HashMap<String, Connection> allUsers = new HashMap<>();
    private ArrayList<Integer> endedGames = new ArrayList<>();
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
        ArrayList<String> users = gamesToUsers.get(gameID);
        for(int i = 0; i < users.size(); i++){
            if (Objects.equals(users.get(i), authToken)){
                users.remove(i);
                break;
            }
        }
        gamesToUsers.put(gameID, users);
        allUsers.remove(authToken);
    }
    public void endGame(Integer gameID){
        if(!gameEnded(gameID)){endedGames.add(gameID);}
    }
    public boolean gameEnded(Integer gameID){
        for(Integer game : endedGames){
            if (Objects.equals(game, gameID)){return true;}
        }
        return false;
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
