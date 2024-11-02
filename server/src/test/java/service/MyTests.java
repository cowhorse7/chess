package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class MyTests {//I've refactored my code so much
    // and changed return and exception types for the StandardAPITests that many of these no longer work
    private static final UserDAO USER_DATA_ACCESS = new MemoryUserDAO();
    private static final AuthDAO AUTH_DATA_ACCESS = new MemoryAuthDAO();
    private static final GameDAO GAME_DATA_ACCESS = new MemoryGameDAO();
    private static final Service SERVICE = new Service(USER_DATA_ACCESS, AUTH_DATA_ACCESS, GAME_DATA_ACCESS);

    @BeforeEach
    public void clear() {
        SERVICE.clear();
    }

    @Test
    @DisplayName("First Test")
    public void registerUser() throws ServiceException {
        String username = "HiImNew";
        String email = "yo@yahoo.com";
        String password = "yoho";
        UserData newUser = new UserData(username, password, email);
        AuthData returnVal = SERVICE.registerUser(newUser);
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
        AuthData user1 = SERVICE.registerUser(newUser1);
        AuthData user2 = SERVICE.registerUser(newUser2);
        AuthData user3 = SERVICE.registerUser(newUser3);
        AuthData user4 = SERVICE.registerUser(newUser4);
        AuthData user5 = SERVICE.registerUser(newUser5);
        Assertions.assertEquals(5, USER_DATA_ACCESS.getDataBaseSize());
    }

    @Test
    @DisplayName("RegisterSameUser")
    public void reRegister() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        Assertions.assertThrows(ServiceException.class, () -> {
            SERVICE.registerUser(newUser1);
        });
    }

    @Test
    @DisplayName("Login")
    public void login() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        Assertions.assertThrows(ServiceException.class, () -> {
            SERVICE.loginUser(newUser1.username(), newUser1.password());
        });
    }

    @Test
    @DisplayName("Logout")
    public void logout() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        int size = AUTH_DATA_ACCESS.getDatabaseSize();
        SERVICE.logoutUser(user1.authToken());
        Assertions.assertEquals(size - 1, AUTH_DATA_ACCESS.getDatabaseSize());
    }

    @Test
    @DisplayName("LogoutAndIn")
    public void logs() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        int size = AUTH_DATA_ACCESS.getDatabaseSize();
        SERVICE.logoutUser(user1.authToken());
        SERVICE.loginUser(newUser1.username(), newUser1.password());
        Assertions.assertEquals(size, AUTH_DATA_ACCESS.getDatabaseSize());
    }

    @Test
    @DisplayName("WrongPassword")
    public void passwordErr() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        SERVICE.logoutUser(user1.authToken());
        Assertions.assertThrows(ServiceException.class, () -> {
            SERVICE.loginUser(newUser1.username(), "b");
        });
    }

    @Test
    @DisplayName("List")
    public void list() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        ListGamesResponse listOfGames = SERVICE.listGames(user1.authToken());
        Assertions.assertNull(listOfGames);
    }

    @Test
    @DisplayName("Create")
    public void create() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        SERVICE.createGame(user1.authToken(), "hey");
        ListGamesResponse listOfGames = SERVICE.listGames(user1.authToken());
        Assertions.assertNotNull(listOfGames);
    }

    @Test
    @DisplayName("Create Many")
    public void createMany() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        SERVICE.createGame(user1.authToken(), "hey");
        SERVICE.createGame(user1.authToken(), "yo");
        SERVICE.createGame(user1.authToken(), "five");
        SERVICE.createGame(user1.authToken(), "six");
        SERVICE.createGame(user1.authToken(), "oh");
        ListGamesResponse listOfGames = SERVICE.listGames(user1.authToken());
        Assertions.assertEquals(5, GAME_DATA_ACCESS.getGameDatabaseSize());
    }

    @Test
    @DisplayName("Join")
    public void join() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        SERVICE.createGame(user1.authToken(), "hey");
        SERVICE.createGame(user1.authToken(), "yo");
        SERVICE.joinGame(user1.authToken(), "white", 2);
        ListGamesResponse listOfGames = SERVICE.listGames(user1.authToken());
        System.out.println(listOfGames);
        Assertions.assertEquals(2, GAME_DATA_ACCESS.getGameDatabaseSize());
    }

    @Test
    @DisplayName("No Join-Service Exceptions")
    public void noJoin() throws ServiceException {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        UserData newUser2 = new UserData("b", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        AuthData user2 = SERVICE.registerUser(newUser2);
        SERVICE.createGame(user1.authToken(), "hey");
        SERVICE.createGame(user1.authToken(), "yo");
        SERVICE.joinGame(user1.authToken(), "white", 2);
        SERVICE.joinGame(user2.authToken(), "white", 1);
        ListGamesResponse listOfGames = SERVICE.listGames(user1.authToken());
        System.out.println(listOfGames);
        Assertions.assertThrows(ServiceException.class, () -> {
            SERVICE.joinGame(user2.authToken(), "white", 2);
        });
        Assertions.assertThrows(ServiceException.class, () -> {
            SERVICE.joinGame(user2.authToken(), "black", 1);
        });
        Assertions.assertThrows(ServiceException.class, () -> {
            SERVICE.joinGame(user2.authToken(), "white", 1);
        });
        Assertions.assertThrows(ServiceException.class, () -> {
            SERVICE.joinGame(user2.authToken(), "black", 4);
        });

    }
}