package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;

public class SQLUserDAO implements UserDAO{
    public SQLUserDAO() throws Exception{
        configureDatabase();
    }
    public void clear() throws Exception {
        String statement = "TRUNCATE user";
        executeUpdate(statement);
    }

    public void createUser(UserData newUser) throws Exception {
        var statement = "INSERT INTO user (username, password, email) VALUES (?,?,?)";
        executeUpdate(statement, newUser.username(),newUser.password(), newUser.email());
    }

    public UserData getUser(String username) throws Exception {
        try(var conn = DatabaseManager.getConnection()){
            String statement = "SELECT username FROM user WHERE username=?";
            try(var ps = conn.prepareStatement(statement)){
                try(var rs = ps.executeQuery()){
                    return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                }
            }catch (SQLException e){
                throw new Exception(String.format("Unable to read data: %s", e.getMessage()));
            }
        }
    }

    public int getDataBaseSize() {
        return 0;
    }

    private final String[] createStatements = {
            """
                CREATE TABLE IF NOT EXISTS user (
                    'username' varchar(128) NOT NULL,
                    'password' varchar(128) NOT NULL,
                    'email' varchar(128) NOT NULL,
                   PRIMARY KEY ('username'),
                   INDEX(email)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
    private void executeUpdate(String statement, Object... params) throws Exception{
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                for(int i = 0; i < params.length; i++){
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i+1,p);
                    else {throw new Exception("parameter not a String");}
                }
                ps.executeUpdate();
            }catch (SQLException e){
                throw new Exception(String.format("unable to update database: %s, %s", statement, e.getMessage()));
            }
            }
    }
    private void configureDatabase() throws Exception {
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){
            for(var statement: createStatements){
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex){
            throw new Exception(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
