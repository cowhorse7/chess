package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeAll;

public class MyTests {
    private static UserDAO USER_DATA_ACCESS;
    private static AuthDAO AUTH_DATA_ACCESS;
    private static GameDAO GAME_DATA_ACCESS;
    private static Service SERVICE;

    @BeforeAll
    public static void setup(){
        try {
            USER_DATA_ACCESS = new SQLUserDAO();
            AUTH_DATA_ACCESS = new MemoryAuthDAO();
            GAME_DATA_ACCESS = new MemoryGameDAO();
            SERVICE = new Service(USER_DATA_ACCESS, AUTH_DATA_ACCESS, GAME_DATA_ACCESS);
        }catch(Exception e){
            System.out.print("Initialization problem");
        }
    }

    @BeforeEach
    public void clear() throws Exception {
        SERVICE.clear();
    }

    @Test
    @DisplayName("First Test")
    public void registerUser() throws Exception {
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
    public void registrations() throws Exception {
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
        Assertions.assertEquals(newUser4, USER_DATA_ACCESS.getUser("d"));
    }

    @Test
    @DisplayName("RegisterSameUser")
    public void reRegister() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        Assertions.assertThrows(Exception.class, () -> {
            SERVICE.registerUser(newUser1);
        });
    }

    @Test
    @DisplayName("Login")
    public void login() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        Assertions.assertEquals(USER_DATA_ACCESS.getUser("a"), newUser1);
    }

    @Test
    @DisplayName("Logout")
    public void logout() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        SERVICE.logoutUser(user1.authToken());
        Assertions.assertNull(AUTH_DATA_ACCESS.getAuthByToken(user1.authToken()));
    }

    @Test
    @DisplayName("LogoutAndIn")
    public void logs() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        SERVICE.logoutUser(user1.authToken());
        Assertions.assertNotNull(SERVICE.loginUser(newUser1.username(), newUser1.password()));
    }

    @Test
    @DisplayName("FailedLogout")
    public void badLogout() throws Exception {
        registerUser();
        Assertions.assertThrows(Exception.class, ()->{SERVICE.logoutUser("bogusAuth");});
    }

    @Test
    @DisplayName("WrongPassword")
    public void passwordErr() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        SERVICE.logoutUser(user1.authToken());
        Assertions.assertThrows(Exception.class, () -> {
            SERVICE.loginUser(newUser1.username(), "b");
        });
    }

    @Test
    @DisplayName("EmptyList")
    public void list() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        ListGamesResponse listOfGames = SERVICE.listGames(user1.authToken());
        Assertions.assertEquals(0, listOfGames.listSize());
    }

    @Test
    @DisplayName("Create")
    public void create() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        SERVICE.createGame(user1.authToken(), "hey");
        ListGamesResponse listOfGames = SERVICE.listGames(user1.authToken());
        Assertions.assertNotNull(listOfGames);
    }

    @Test
    @DisplayName("Create Many")
    public void createManyGames() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        SERVICE.createGame(user1.authToken(), "hey");
        SERVICE.createGame(user1.authToken(), "yo");
        SERVICE.createGame(user1.authToken(), "five");
        SERVICE.createGame(user1.authToken(), "six");
        SERVICE.createGame(user1.authToken(), "oh");
        Assertions.assertEquals(5, GAME_DATA_ACCESS.getGameDatabaseSize());
    }

    @Test
    @DisplayName("Join")
    public void join() throws Exception {
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
    @DisplayName("JoinOverOtherPlayer")
    public void noJoin() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        UserData newUser2 = new UserData("b", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        AuthData user2 = SERVICE.registerUser(newUser2);
        SERVICE.createGame(user1.authToken(), "hey");
        SERVICE.createGame(user1.authToken(), "yo");
        SERVICE.joinGame(user1.authToken(), "WHITE", 2);
        SERVICE.joinGame(user2.authToken(), "WHITE", 1);
        Assertions.assertThrows(Exception.class, () -> {
            SERVICE.joinGame(user2.authToken(), "WHITE", 2);
        });
    }
}