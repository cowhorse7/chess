package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class MyTests {
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

    @BeforeEach
    public void clear() throws Exception {
        service.clear();
    }

    @Test
    @DisplayName("Register")
    public void registerUser() throws Exception {
        String username = "HiImNew";
        String email = "yo@yahoo.com";
        String password = "yoho";
        UserData newUser = new UserData(username, password, email);
        AuthData returnVal = service.registerUser(newUser);
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
        AuthData user1 = service.registerUser(newUser1);
        AuthData user2 = service.registerUser(newUser2);
        AuthData user3 = service.registerUser(newUser3);
        AuthData user4 = service.registerUser(newUser4);
        AuthData user5 = service.registerUser(newUser5);
        Assertions.assertNotNull(userDataAccess.getUser("d"));
    }

    @Test
    @DisplayName("NoRegisterSameUser")
    public void reRegister() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = service.registerUser(newUser1);
        Assertions.assertThrows(Exception.class, () -> {
            service.registerUser(newUser1);
        });
    }
    @Test
    @DisplayName("NullFailRegister")
    public void nullRegisterAttempts() throws Exception {
        UserData newUser1 = new UserData("a", null, "c@c");
        UserData newUser2 = new UserData(null, "bbb", "c@c");
        UserData newUser3 = new UserData("a", "bbb", null);

        Assertions.assertThrows(Exception.class, () -> {
            service.registerUser(newUser1);});
        Assertions.assertThrows(Exception.class, () -> {
            service.registerUser(newUser2);});
        Assertions.assertThrows(Exception.class, () -> {
            service.registerUser(newUser3);});
    }
    @Test
    @DisplayName("Login")
    public void login() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        service.registerUser(newUser1);
        Assertions.assertNotNull(service.loginUser(newUser1.username(), newUser1.password()));}
    @Test
    @DisplayName("UserDoesNotExist")
    public void noLogin() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        Assertions.assertThrows(Exception.class, () -> {
            service.loginUser("bogus", newUser1.password());});
    }

    @Test
    @DisplayName("Logout")
    public void logout() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = service.registerUser(newUser1);
        service.logoutUser(user1.authToken());
        Assertions.assertNull(authDataAccess.getAuthByToken(user1.authToken()));
    }

    @Test
    @DisplayName("FailedLogout")
    public void badLogout() throws Exception {
        registerUser();
        Assertions.assertThrows(Exception.class, ()->{
            service.logoutUser("bogusAuth");});
    }

    @Test
    @DisplayName("WrongPassword")
    public void passwordErr() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = service.registerUser(newUser1);
        service.logoutUser(user1.authToken());
        Assertions.assertThrows(Exception.class, () -> {
            service.loginUser(newUser1.username(), "b");
        });
    }

    @Test
    @DisplayName("EmptyList")
    public void emptyList() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = service.registerUser(newUser1);
        ListGamesResponse listOfGames = service.listGames(user1.authToken());
        Assertions.assertEquals(0, listOfGames.listSize());
    }
    @Test
    @DisplayName("BadAuthOnList")
    public void noList() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = service.registerUser(newUser1);
        Assertions.assertThrows(Exception.class, ()->{
            service.listGames("bogusAuth");});
    }
    @Test
    @DisplayName("LongerGameList")
    public void list() throws Exception{
        createManyGames();
        UserData newUser1 = new UserData("b", "bbb", "c@c");
        AuthData user1 = service.registerUser(newUser1);
        ListGamesResponse listOfGames = service.listGames(user1.authToken());
        Assertions.assertEquals(5, listOfGames.listSize());
    }
    @Test
    @DisplayName("CreateGame")
    public void create() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = service.registerUser(newUser1);
        int game1id = service.createGame(user1.authToken(), "hey");
        Assertions.assertEquals("hey", gameDataAccess.getGame(game1id).gameName());
    }

    @Test
    @DisplayName("CreateMany")
    public void createManyGames() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = service.registerUser(newUser1);
        int game1id = service.createGame(user1.authToken(), "hey");
        service.createGame(user1.authToken(), "yo");
        service.createGame(user1.authToken(), "five");
        service.createGame(user1.authToken(), "six");
        service.createGame(user1.authToken(), "oh");
        Assertions.assertEquals("hey", gameDataAccess.getGame(game1id).gameName());
    }

    @Test
    @DisplayName("Join")
    public void join() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        AuthData user1 = service.registerUser(newUser1);
        int game1id = service.createGame(user1.authToken(), "hey");
        service.createGame(user1.authToken(), "yo");
        service.joinGame(user1.authToken(), "WHITE", game1id);
        Assertions.assertEquals(game1id, gameDataAccess.getGame(game1id).gameID());
        Assertions.assertEquals(user1.username(), gameDataAccess.getGame(game1id).whiteUsername());
    }

    @Test
    @DisplayName("CantJoinOverOtherPlayer")
    public void noJoin() throws Exception {
        UserData newUser1 = new UserData("a", "bbb", "c@c");
        UserData newUser2 = new UserData("b", "bbb", "c@c");
        AuthData user1 = service.registerUser(newUser1);
        AuthData user2 = service.registerUser(newUser2);
        int game1id = service.createGame(user1.authToken(), "hey");
        int game2id = service.createGame(user1.authToken(), "yo");
        service.joinGame(user1.authToken(), "WHITE", game2id);
        service.joinGame(user2.authToken(), "WHITE", game1id);
        Assertions.assertThrows(Exception.class, () -> {
            service.joinGame(user2.authToken(), "WHITE", game2id);
        });
    }
}