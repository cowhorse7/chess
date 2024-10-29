package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void createAuth(AuthData newAuth);
    public boolean getAuth(AuthData userAuth);
    public void deleteAuth(AuthData userAuth);
    public void clear();
}
