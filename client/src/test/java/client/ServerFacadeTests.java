package client;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }
    @BeforeEach
    void preStuff() throws Exception {
        serverFacade.clear();
    }
    @Test
    public void createUserPos() throws Exception {
        UserData newUser = new UserData("username","password", "email@email");
        AuthData auth =  serverFacade.createUser(newUser);
        Assertions.assertEquals(auth.username(), "username");
    }
    @Test
    public void createUserNeg() throws Exception {
        UserData newUser = new UserData("username","password", null);
        Assertions.assertThrows(Exception.class, ()-> serverFacade.createUser(newUser));
    }
    @Test
    public void loginUserPos() throws Exception {
        createUserPos();
        UserData newUser = new UserData("username","password", null);
        AuthData auth =  serverFacade.loginUser(newUser);
        Assertions.assertEquals(auth.username(), "username");
    }
    @Test
    public void loginUserNeg() throws Exception {
        createUserPos();
        UserData newUser = new UserData("username",null, null);
        Assertions.assertThrows(Exception.class, ()-> serverFacade.loginUser(newUser));
    }
    @Test
    public void logoutUserPos() throws Exception {
        loginUserPos();
        Assertions.assertDoesNotThrow(()->serverFacade.logoutUser());
    }
    @Test
    public void logoutUserNeg() throws Exception {
        logoutUserPos();
        Assertions.assertThrows(Exception.class, ()->serverFacade.logoutUser());
    }
    @Test
    public void createGamePos() throws Exception {
        createUserPos();
        Assertions.assertDoesNotThrow(()->serverFacade.createChessGame("yoyo"));
    }
    @Test
    public void createGameNeg() throws Exception {
        createUserPos();
        Assertions.assertThrows(Exception.class, ()->serverFacade.createChessGame(null));
    }
    @Test
    public void listGamesPos() throws Exception {
        createUserPos();
        Assertions.assertDoesNotThrow(()->serverFacade.listGames());
        System.out.print(serverFacade.listGames());
    }
    @Test
    public void listGamesWithJoin() throws Exception{
        joinGamePos();
        Assertions.assertDoesNotThrow(()->serverFacade.listGames());
        System.out.print(serverFacade.listGames());
    }
    @Test
    public void listGamesNeg() throws Exception {
        logoutUserPos();
        Assertions.assertThrows(Exception.class, ()->serverFacade.listGames());
    }
    @Test
    public void joinGamePos() throws Exception {
        listGamesPos();
        serverFacade.createChessGame("heyy");
        serverFacade.listGames();
        Assertions.assertDoesNotThrow(()->serverFacade.joinGame(1, "white"));
    }
    @Test
    public void joinGameNeg() throws Exception {
        joinGamePos();
        serverFacade.logoutUser();
        serverFacade.createUser(new UserData("yo","yoyo","yo@yo"));
        Assertions.assertThrows(Exception.class, ()->serverFacade.joinGame(1, "white"));
    }

}
