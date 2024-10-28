package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{
    public void clear() {

    }
    public void createUser(UserData newUser) {

    }
    public String getUser(UserData username) {
        //search for username in database
        return username.username();
    }
}
