package dataaccess;

import model.GameData;
import model.UserData;

import java.sql.SQLException;
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
                    `jsonGame` TEXT DEFAULT NULL,
                   PRIMARY KEY (`gameID`),
                   INDEX(gameName)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }
    public void createGame(int gameID, GameData newGame) throws Exception {
        String statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?,?,?,?,?)";
        DatabaseManager.executeUpdate(statement, gameID, newGame.whiteUsername(), newGame.blackUsername(), newGame.gameName(), newGame.game());
    }
    public GameData getGame(int id) throws Exception {
        try(var conn = DatabaseManager.getConnection()){
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM user WHERE gameID=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setInt(1, id);
                try(var rs = ps.executeQuery()){
                    if(rs.next()) {return null; //FIXME!!!!
//                        return new GameData(rs.getInt("gameID"),
//                                rs.getString("whiteUsername"),
//                                rs.getString("blackUsername"),
//                                rs.getString("gameName"),
//                                rs.getString("game"));
                    }else{return null;}
                }
            }catch (SQLException e){
                throw new Exception(String.format("Unable to read data: %s", e.getMessage()));
            }
        }
    }
    public HashMap<Integer, GameData> listGames() {
        return null;
    }
    public void updateGame(int gameID, GameData newGame) throws Exception {
        String statement = "UPDATE games SET ?=? WHERE id=?"; //FIXME!!!
        DatabaseManager.executeUpdate(statement, gameID);
    }
    public void clear() throws Exception {
        String statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }
    public int getGameDatabaseSize() {
        return 0;
    }
}
