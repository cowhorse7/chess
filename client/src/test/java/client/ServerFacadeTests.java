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
    public void createGamePos() throws Exception {}
    public void createGameNeg() throws Exception {}
    public void loginUserPos() throws Exception {
        UserData newUser = new UserData("username","password", null);
        AuthData auth =  serverFacade.createUser(newUser);
        Assertions.assertEquals(auth.username(), "username");
    }
    public void loginUserNeg() throws Exception {
        UserData newUser = new UserData("username",null, null);
        Assertions.assertThrows(Exception.class, ()-> serverFacade.loginUser(newUser));
    }
    public void logoutUserPos() throws Exception {}
    public void logoutUserNeg() throws Exception {}
    public void listGamesPos() throws Exception {}
    public void listGamesNeg() throws Exception {}
    public void joinGamePos() throws Exception {}
    public void joinGameNeg() throws Exception {}
    public void clear() throws Exception {}

}
