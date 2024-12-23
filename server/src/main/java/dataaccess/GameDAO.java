package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {
    void createGame(int gameID, GameData newGame) throws Exception;
    GameData getGame(int id) throws Exception;
    HashMap<Integer, GameData> listGames() throws Exception;
    void updateGame(int gameID, GameData newGame) throws Exception;
    void clear() throws Exception;
    void removeGame(Integer gameID) throws Exception;
}
