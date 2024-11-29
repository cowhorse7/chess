package server;
import org.eclipse.jetty.websocket.api.Session;

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
    public void remove(String authToken, Integer gameID){
        //Integer index = findUser(authToken, gameID);
        gamesToUsers.get(gameID).remove(authToken);
    }
//    private Integer findUser(String authToken, Integer gameID){
//        ArrayList<String> users = gamesToUsers.get(gameID);
//        String auth = "";
//        int i = 0;
//        for(i = 0; i < users.size(); i++) {
//            auth = users.get(i);
//            if(Objects.equals(auth, authToken)){
//                break;
//            }
//        }
//        return i;
//    }
}
