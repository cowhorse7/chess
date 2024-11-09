package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO{
    private final ArrayList<AuthData> authDatabase = new ArrayList<>();
    public void createAuth(AuthData newAuth) {
        authDatabase.add(newAuth);
    }
    public AuthData getAuthByToken(String userAuth){
        AuthData foundAuth = null;
        for (int i = 0; i < authDatabase.size(); i++){
            if(Objects.equals(authDatabase.get(i).authToken(), userAuth)){
                foundAuth = authDatabase.get(i);
                break;
            }
        }
        return foundAuth;
    }
    public void deleteAuth(AuthData userAuth) {
        authDatabase.remove(userAuth);
    }

    public void clear() {
        authDatabase.clear();
    }
}
