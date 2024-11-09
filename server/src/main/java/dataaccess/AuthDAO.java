package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData newAuth);
    AuthData getAuthByToken(String userAuth);
    void deleteAuth(AuthData userAuth);
    void clear();
}
