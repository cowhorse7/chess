package dataaccess;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class SQLGameDAO implements GameDAO{
    private final Gson serializer = new Gson();
    public SQLGameDAO() throws Exception{
        String[] createStatements = {
                """
                CREATE TABLE IF NOT EXISTS games (
                    `gameID` int NOT NULL,
                    `jsonGame` TEXT DEFAULT NULL,
                   PRIMARY KEY (`gameID`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }
    public void createGame(int gameID, GameData newGame) throws Exception {
        String statement = "INSERT INTO games (gameID, jsonGame) VALUES (?,?)";
        DatabaseManager.executeUpdate(statement, gameID, serializer.toJson(newGame));
    }
    public GameData getGame(int id) throws Exception {
        try(var conn = DatabaseManager.getConnection()){
            String statement = "SELECT jsonGame FROM games WHERE gameID=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setInt(1, id);
                try(var rs = ps.executeQuery()){
                    if(rs.next()) {
                        return serializer.fromJson(rs.getString("jsonGame"), GameData.class);
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
        String statement = "UPDATE games SET jsonGame=? WHERE gameID=?";
        DatabaseManager.executeUpdate(statement, serializer.toJson(newGame), gameID);
    }
    public void clear() throws Exception {
        String statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }
}
