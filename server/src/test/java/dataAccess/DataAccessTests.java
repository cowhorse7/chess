package dataAccess;

import dataaccess.*;
import org.junit.jupiter.api.*;
import service.Service;

public class DataAccessTests {
        private static UserDAO userDataAccess;
        private static AuthDAO authDataAccess;
        private static GameDAO gameDataAccess;
        private static Service service;

        @BeforeAll
        public static void setup(){
            try {
                userDataAccess = new SQLUserDAO();
                authDataAccess = new SQLAuthDAO();
                gameDataAccess = new SQLGameDAO();
                service = new Service(userDataAccess, authDataAccess, gameDataAccess);
            }catch(Exception e){
                System.out.print("Initialization problem");
            }
        }
}
