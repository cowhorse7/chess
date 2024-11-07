package dataaccess;

import model.UserData;

public interface UserDAO {
    void clear() throws Exception;
    void createUser(UserData newUser) throws Exception;
    UserData getUser(String username) throws Exception;
    int getDataBaseSize();
}
