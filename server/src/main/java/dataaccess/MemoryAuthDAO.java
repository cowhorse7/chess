package dataaccess;

import model.AuthData;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO{
    private final HashSet<AuthData> authDatabase = new HashSet<>();
    public void createAuth(AuthData newAuth) {
        authDatabase.add(newAuth);
    }

    public boolean getAuth(AuthData userAuth) {
        if (authDatabase.contains(userAuth)){return true;}
        else return false;
    }

    public void deleteAuth(AuthData userAuth) {
        authDatabase.remove(userAuth);
    }

    public void clear() {
        authDatabase.clear();
    }
}
