package dataaccess;

import model.GameData;

import java.util.HashMap;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() throws Exception{
        String[] createStatements = {
                """
                CREATE TABLE IF NOT EXISTS games (
                    `gameID` int NOT NULL,
                    `whiteUsername` varchar(128) NOT NULL,
                    `blackUsername` varchar(128) NOT NULL,
                    `gameName` varchar(128) NOT NULL,
                    `game` varchar(128) NOT NULL,
                   PRIMARY KEY (`gameID`),
                   INDEX(gameName)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }
    public void createGame(int gameID, GameData newGame) {

    }
    public GameData getGame(int id) {
        return null;
    }
    public HashMap<Integer, GameData> listGames() {
        return null;
    }
    public void updateGame(int gameID, GameData newGame) {

    }
    public void clear() throws Exception {
        String statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }
    public int getGameDatabaseSize() {
        return 0;
    }
}
