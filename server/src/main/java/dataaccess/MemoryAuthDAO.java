package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private final HashMap<String, AuthData> authDatabase = new HashMap<>();
    public void createAuth(AuthData newAuth) {
        authDatabase.put(newAuth.username(), newAuth);
    }

    public AuthData getAuth(String user) {
        return authDatabase.get(user);
    }
    public void deleteAuth(String userAuth) {
        authDatabase.remove(userAuth);
    }

    public void clear() {
        authDatabase.clear();
    }
}
