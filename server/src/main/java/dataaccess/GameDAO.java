package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {
    void createGame(int gameID, GameData newGame) throws Exception;
    GameData getGame(int id) throws Exception;
    HashMap<Integer, GameData> listGames();
    void updateGame(int gameID, GameData newGame) throws Exception;
    void clear() throws Exception;
    int getGameDatabaseSize();
}
