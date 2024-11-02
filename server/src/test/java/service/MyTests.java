package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class MyTests {//I've refactored my code so much and changed return and exception types for the StandardAPITests that many of these no longer work
    private static final UserDAO userDataAccess = new MemoryUserDAO();
    private static final AuthDAO authDataAccess = new MemoryAuthDAO();
    private static final GameDAO gameDataAccess = new MemoryGameDAO();
    private static final Service myService = new Service(userDataAccess, authDataAccess, gameDataAccess);

    @BeforeEach
    public void clear() {
        myService.clear();
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
        Assertions.assertThrows(ServiceException.class, () -> {
            myService.registerUser(newUser1);
        });
    }

    @Test
    @DisplayName("Login")
    public void login() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = myService.registerUser(newUser1);
        Assertions.assertThrows(ServiceException.class, () -> {
            myService.loginUser(newUser1.username(), newUser1.password());
        });
    }

    @Test
    @DisplayName("Logout")
    public void logout() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = myService.registerUser(newUser1);
        int size = authDataAccess.getDatabaseSize();
        myService.logoutUser(user1.authToken());
        Assertions.assertEquals(size - 1, authDataAccess.getDatabaseSize());
    }

    @Test
    @DisplayName("LogoutAndIn")
    public void logs() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = myService.registerUser(newUser1);
        int size = authDataAccess.getDatabaseSize();
        myService.logoutUser(user1.authToken());
        myService.loginUser(newUser1.username(), newUser1.password());
        Assertions.assertEquals(size, authDataAccess.getDatabaseSize());
    }

    @Test
    @DisplayName("WrongPassword")
    public void passwordErr() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = myService.registerUser(newUser1);
        myService.logoutUser(user1.authToken());
        Assertions.assertThrows(ServiceException.class, () -> {
            myService.loginUser(newUser1.username(), "b");
        });
    }

    @Test
    @DisplayName("List")
    public void list() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = myService.registerUser(newUser1);
        ListGamesResponse listOfGames = myService.listGames(user1.authToken());
        Assertions.assertNull(listOfGames);
    }

    @Test
    @DisplayName("Create")
    public void create() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = myService.registerUser(newUser1);
        myService.createGame(user1.authToken(), "hey");
        ListGamesResponse listOfGames = myService.listGames(user1.authToken());
        Assertions.assertNotNull(listOfGames);
    }

    @Test
    @DisplayName("Create Many")
    public void createMany() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = myService.registerUser(newUser1);
        myService.createGame(user1.authToken(), "hey");
        myService.createGame(user1.authToken(), "yo");
        myService.createGame(user1.authToken(), "five");
        myService.createGame(user1.authToken(), "six");
        myService.createGame(user1.authToken(), "oh");
        ListGamesResponse listOfGames = myService.listGames(user1.authToken());
        Assertions.assertEquals(5, gameDataAccess.getGameDatabaseSize());
    }

    @Test
    @DisplayName("Join")
    public void join() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = myService.registerUser(newUser1);
        myService.createGame(user1.authToken(), "hey");
        myService.createGame(user1.authToken(), "yo");
        myService.joinGame(user1.authToken(), "white", 2);
        ListGamesResponse listOfGames = myService.listGames(user1.authToken());
        System.out.println(listOfGames);
        Assertions.assertEquals(2, gameDataAccess.getGameDatabaseSize());
    }

    @Test
    @DisplayName("No Join-Service Exceptions")
    public void noJoin() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        UserData newUser2 = new UserData("b", "bbb", "c@c");
        AuthData user1 = myService.registerUser(newUser1);
        AuthData user2 = myService.registerUser(newUser2);
        myService.createGame(user1.authToken(), "hey");
        myService.createGame(user1.authToken(), "yo");
        myService.joinGame(user1.authToken(), "white", 2);
        myService.joinGame(user2.authToken(), "white", 1);
        ListGamesResponse listOfGames = myService.listGames(user1.authToken());
        System.out.println(listOfGames);
        Assertions.assertThrows(ServiceException.class, () -> {
            myService.joinGame(user2.authToken(), "white", 2);
        });
        Assertions.assertThrows(ServiceException.class, () -> {
            myService.joinGame(user2.authToken(), "black", 1);
        });
        Assertions.assertThrows(ServiceException.class, () -> {
            myService.joinGame(user2.authToken(), "white", 1);
        });
        Assertions.assertThrows(ServiceException.class, () -> {
            myService.joinGame(user2.authToken(), "black", 4);
        });

    }
}