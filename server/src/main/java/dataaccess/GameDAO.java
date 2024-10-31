package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {
    public void createGame(int gameID, GameData newGame);
    public GameData getGame(int ID);
    public HashMap<Integer, GameData> listGames();
    public void updateGame(GameData newGame);
    public void clear();
    public int getGameDatabaseSize();
}
