package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.Service;

//true, equals, null, notnull, throws, doesnotthrow
//assertions.assert
public class MyTests {
    private static UserDAO userDataAccess = new MemoryUserDAO();
    private static AuthDAO authDataAccess = new MemoryAuthDAO();
    private static GameDAO gameDataAccess = new MemoryGameDAO();
    private static Service myService = new Service(userDataAccess, authDataAccess, gameDataAccess);

    @BeforeEach
    public void clear(){
        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
    }
    @Test
    @DisplayName("First Test")
    public void registerUser() throws ServiceException {
        String username = "HiImNew";
        String email = "yo@yahoo.com";
        String password = "yoho";
        UserData newUser = new UserData(username, password, email);
        AuthData returnVal = myService.registerUser(newUser);
        Assertions.assertEquals(username, returnVal.username());
        Assertions.assertNotNull(returnVal.authToken());
    }
    @Test
    @DisplayName("RegisterManyUsers")
    public void registrations() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        UserData newUser2 = new UserData("b", "bbb", "c@c");
        UserData newUser3 = new UserData("c", "bbb", "c@c");
        UserData newUser4 = new UserData("d", "bbb", "c@c");
        UserData newUser5 = new UserData("e", "bbb", "c@c");
        UserData newUser6 = new UserData("a", "bbb", "c@c");
        AuthData user1 = myService.registerUser(newUser1);
        AuthData user2 = myService.registerUser(newUser2);
        AuthData user3 = myService.registerUser(newUser3);
        AuthData user4 = myService.registerUser(newUser4);
        AuthData user5 = myService.registerUser(newUser5);
        Assertions.assertEquals(5, userDataAccess.getDataBaseSize());
    }
    @Test
    @DisplayName("RegisterSameUser")
    public void reRegister() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = myService.registerUser(newUser1);
        Assertions.assertThrows(SecurityException.class, ()->{myService.registerUser(newUser1);});
    }
}