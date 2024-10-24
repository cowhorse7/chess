package dataaccess;

import model.GameData;

public interface GameDAO {
    public void createGame(GameData newGame);
    public void getGame(int ID);
    public void listGames();
    public void updateGame(GameData newGame);
    public void clear();
}
