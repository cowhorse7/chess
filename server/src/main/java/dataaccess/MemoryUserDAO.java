package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    private final HashMap<String, UserData> dataBase = new HashMap<>();
    public void clear() {
        dataBase.clear();
    }
    public void createUser(UserData newUser) {
        dataBase.put(newUser.username(), newUser);
    }
    public UserData getUser(String username) {
        return dataBase.get(username);
    }

    public int getDataBaseSize(){
        return dataBase.size();
    }
}
