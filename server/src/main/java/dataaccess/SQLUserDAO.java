package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws Exception{
        String[] createStatements = {
                """
                CREATE TABLE IF NOT EXISTS user (
                    `username` varchar(128) NOT NULL,
                    `password` varchar(128) NOT NULL,
                    `email` varchar(128) NOT NULL,
                   PRIMARY KEY (`username`),
                   INDEX(email)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }
    public void clear() throws Exception {
        String statement = "TRUNCATE user";
        DatabaseManager.executeUpdate(statement);
    }

    public void createUser(UserData newUser) throws Exception {
        var statement = "INSERT INTO user (username, password, email) VALUES (?,?,?)";
        DatabaseManager.executeUpdate(statement, newUser.username(),newUser.password(), newUser.email());
    }

    public UserData getUser(String username) throws Exception {
        try(var conn = DatabaseManager.getConnection()){
            String statement = "SELECT username, password, email FROM user WHERE username=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try(var rs = ps.executeQuery()){
                    if(rs.next()) {
                        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    }else{return null;}
                }
            }catch (SQLException e){
                throw new Exception(String.format("Unable to read data: %s", e.getMessage()));
            }
        }
    }
}
