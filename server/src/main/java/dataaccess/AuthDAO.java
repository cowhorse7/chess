package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void createAuth(AuthData newAuth);
    public AuthData getAuthByToken(String userAuth);
    public AuthData getAuthByUser(String username);
    public void deleteAuth(AuthData userAuth);
    public void clear();
    public int getDatabaseSize();
}
