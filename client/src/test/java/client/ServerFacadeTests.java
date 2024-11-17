package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }
    @Test
    public void createUserPos(){}
    public void createUserNeg(){}
    public void createGamePos(){}
    public void createGameNeg(){}
    public void loginUserPos(){}
    public void loginUserNeg(){}
    public void logoutUserPos(){}
    public void logoutUserNeg(){}
    public void listGamesPos(){}
    public void listGamesNeg(){}
    public void joinGamePos(){}
    public void joinGameNeg(){}
    public void clear(){}

}
