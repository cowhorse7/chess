package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws Exception{
        String[] createStatements = {
                """
                CREATE TABLE IF NOT EXISTS auth (
                    `authToken` varchar(128) NOT NULL,
                    `username` varchar(128) NOT NULL,
                   PRIMARY KEY (`authToken`)
                )
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }
    public void createAuth(AuthData newAuth) throws Exception {
        String statement = "INSERT INTO auth (authToken, username) VALUES (?,?)";
        DatabaseManager.executeUpdate(statement, newAuth.authToken(), newAuth.username());
    }

    public AuthData getAuthByToken(String userAuth) throws Exception{
        try(var conn = DatabaseManager.getConnection()){
            String statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1, userAuth);
                try(var rs = ps.executeQuery()){
                    if(rs.next()) {
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }else{return null;}
                }
            }catch (SQLException e){
                throw new Exception(String.format("Unable to read data: %s", e.getMessage()));
            }
        }
    }

    public void deleteAuth(AuthData userAuth) throws Exception {
        if(getAuthByToken(userAuth.authToken()) == null){throw new Exception("No such token");}
        String statement = "DELETE FROM auth WHERE authToken=?";
        DatabaseManager.executeUpdate(statement, userAuth.authToken());
    }
    public void clear() throws Exception {
        String statement = "TRUNCATE auth";
        DatabaseManager.executeUpdate(statement);
    }
}
