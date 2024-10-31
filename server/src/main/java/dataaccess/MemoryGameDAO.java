package dataaccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private HashMap<Integer, GameData>gameDatabase = new HashMap<>();
    public void createGame(int gameID, GameData newGame) {
        gameDatabase.put(gameID, newGame);
    }

    public GameData getGame(int ID) {
        return gameDatabase.get(ID);
    }

    public HashMap<Integer, GameData> listGames() {
        return gameDatabase;
    }

    public void updateGame(GameData newGame) {

    }

    public void clear() {
        gameDatabase.clear();
    }
    public int getGameDatabaseSize(){return gameDatabase.size();}
}
