package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {
    void createGame(int gameID, GameData newGame);
    GameData getGame(int id);
    HashMap<Integer, GameData> listGames();
    void updateGame(int gameID, GameData newGame);
    void clear() throws Exception;
    int getGameDatabaseSize();
}
