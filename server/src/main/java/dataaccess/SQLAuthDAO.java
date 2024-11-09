package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.sql.Statement;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() throws Exception{
        String[] createStatements = {
                """
                CREATE TABLE IF NOT EXISTS auth (
                    `authToken` varchar(128) NOT NULL,
                    `username` varchar(128) NOT NULL,
                   PRIMARY KEY (`authToken`),
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }
    public void createAuth(AuthData newAuth) {

    }

    public AuthData getAuthByToken(String userAuth) {
        return null;
    }

    public void deleteAuth(AuthData userAuth) {

    }
    public void clear() {

    }
}
