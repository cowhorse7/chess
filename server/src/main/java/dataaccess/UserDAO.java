package dataaccess;

import model.UserData;

public interface UserDAO {
    void clear();
    void createUser(UserData newUser);
    UserData getUser(String username);
    /*this bit may not be necessary, I think I should be storing usernames
    and passwords together in one "user" object.
    public void getPassword();
    also
    public void updateUser();??
     */
    int getDataBaseSize();
}
