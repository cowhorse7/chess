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
            AUTH_DATA_ACCESS = new SQLAuthDAO();
            GAME_DATA_ACCESS = new SQLGameDAO();
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
    @DisplayName("Register")
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
        AuthData user1 = SERVICE.registerUser(newUser1);
        AuthData user2 = SERVICE.registerUser(newUser2);
        AuthData user3 = SERVICE.registerUser(newUser3);
        AuthData user4 = SERVICE.registerUser(newUser4);
        AuthData user5 = SERVICE.registerUser(newUser5);
        Assertions.assertNotNull(USER_DATA_ACCESS.getUser("d"));
    }

    @Test
    @DisplayName("NoRegisterSameUser")
    public void reRegister() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        Assertions.assertThrows(Exception.class, () -> {
            SERVICE.registerUser(newUser1);
        });
    }
    @Test
    @DisplayName("NullFailRegister")
    public void nullRegisterAttempts() throws Exception {
        UserData newUser1 = new UserData("a", null, "c@c");
        UserData newUser2 = new UserData(null, "bbb", "c@c");
        UserData newUser3 = new UserData("a", "bbb", null);

        Assertions.assertThrows(Exception.class, () -> {SERVICE.registerUser(newUser1);});
        Assertions.assertThrows(Exception.class, () -> {SERVICE.registerUser(newUser2);});
        Assertions.assertThrows(Exception.class, () -> {SERVICE.registerUser(newUser3);});
    }
    @Test
    @DisplayName("Login")
    public void login() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        SERVICE.registerUser(newUser1);
        Assertions.assertNotNull(SERVICE.loginUser(newUser1.username(), newUser1.password()));}
    @Test
    @DisplayName("UserDoesNotExist")
    public void noLogin() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        Assertions.assertThrows(Exception.class, () -> {SERVICE.loginUser("bogus", newUser1.password());});
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
    @DisplayName("BadAuthOnList")
    public void noList() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        Assertions.assertThrows(Exception.class, ()->{SERVICE.listGames("bogusAuth");});
    }
    @Test
    @DisplayName("CreateGame")
    public void create() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        int game1id = SERVICE.createGame(user1.authToken(), "hey");
        Assertions.assertEquals("hey", GAME_DATA_ACCESS.getGame(game1id).gameName());
    }

    @Test
    @DisplayName("CreateMany")
    public void createManyGames() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        int game1id = SERVICE.createGame(user1.authToken(), "hey");
        SERVICE.createGame(user1.authToken(), "yo");
        SERVICE.createGame(user1.authToken(), "five");
        SERVICE.createGame(user1.authToken(), "six");
        SERVICE.createGame(user1.authToken(), "oh");
        Assertions.assertEquals("hey", GAME_DATA_ACCESS.getGame(game1id).gameName());
    }

    @Test
    @DisplayName("Join")
    public void join() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        int game1id = SERVICE.createGame(user1.authToken(), "hey");
        SERVICE.createGame(user1.authToken(), "yo");
        SERVICE.joinGame(user1.authToken(), "WHITE", game1id);
        Assertions.assertEquals(game1id, GAME_DATA_ACCESS.getGame(game1id).gameID());
        Assertions.assertEquals(user1.username(), GAME_DATA_ACCESS.getGame(game1id).whiteUsername());
    }

    @Test
    @DisplayName("CantJoinOverOtherPlayer")
    public void noJoin() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        UserData newUser2 = new UserData("b", "bbb", "c@c");
        AuthData user1 = SERVICE.registerUser(newUser1);
        AuthData user2 = SERVICE.registerUser(newUser2);
        int game1id = SERVICE.createGame(user1.authToken(), "hey");
        int game2id = SERVICE.createGame(user1.authToken(), "yo");
        SERVICE.joinGame(user1.authToken(), "WHITE", game2id);
        SERVICE.joinGame(user2.authToken(), "WHITE", game1id);
        Assertions.assertThrows(Exception.class, () -> {
            SERVICE.joinGame(user2.authToken(), "WHITE", game2id);
        });
    }
}