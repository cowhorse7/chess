package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData newAuth);
    AuthData getAuthByToken(String userAuth);
    AuthData getAuthByUser(String username);
    void deleteAuth(AuthData userAuth);
    void clear();
    int getDatabaseSize();
}
