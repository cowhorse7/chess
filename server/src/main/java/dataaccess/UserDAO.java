package dataaccess;

import model.UserData;

public interface UserDAO {
    public void clear();
    public void createUser(UserData newUser);
    public UserData getUser(UserData username);
    /*this bit may not be necessary, I think I should be storing usernames
    and passwords together in one "user" object.
    public void getPassword();
    also
    public void updateUser();??
     */
    public int getDataBaseSize();
}
