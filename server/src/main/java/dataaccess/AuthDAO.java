package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void createAuth(AuthData newAuth);
    public AuthData getAuth(String userAuth);
    public void deleteAuth(String userAuth);
    public void clear();
}
