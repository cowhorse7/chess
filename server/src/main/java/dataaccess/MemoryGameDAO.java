package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private final HashMap<Integer, GameData>gameDatabase = new HashMap<>();
    public void createGame(int gameID, GameData newGame) {
        gameDatabase.put(gameID, newGame);
    }
    public GameData getGame(int id) {
        return gameDatabase.get(id);
    }
    public HashMap<Integer, GameData> listGames() {return gameDatabase;}
    public void updateGame(int gameID, GameData newGame) {
        gameDatabase.replace(gameID, newGame);
    }
    public void clear() {
        gameDatabase.clear();
    }
    public int getGameDatabaseSize(){return gameDatabase.size();}
}
