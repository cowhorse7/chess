package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData newAuth) throws Exception;
    AuthData getAuthByToken(String userAuth) throws Exception;
    void deleteAuth(AuthData userAuth) throws Exception;
    void clear() throws Exception;
}
