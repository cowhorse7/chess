package dataaccess;

public interface DataAccess {
    public void clear();
    public void createUser();
    public void getUser();
    /*this bit may not be necessary, I think I should be storing usernames
    and passwords together in one "user" object.
    public void getPassword(){throw new RuntimeException("Not implemented");}
     */
    public void createGame();
    public void getGame();
    public void listGames();
    public void updateGame();
    public void createAuth();
    public void getAuth();
    public void deleteAuth();
}
