package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.ListGamesResponse;
import service.Service;

import java.util.HashMap;

public class DataAccessTests {
    private static UserDAO userDataAccess;
    private static AuthDAO authDataAccess;
    private static GameDAO gameDataAccess;

    @BeforeAll
    public static void setup(){
        try {
            userDataAccess = new SQLUserDAO();
            authDataAccess = new SQLAuthDAO();
            gameDataAccess = new SQLGameDAO();
        }catch(Exception e){
            System.out.print("Initialization problem");
        }
    }

    @AfterEach
    public void clear()throws Exception {
        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
    }
    @Test
    public void userCreatePos() throws Exception {
        userDataAccess.createUser(new UserData("hey", "yo", "yo@yo"));
    }
    @Test
    public void userCreateNeg()throws Exception {
        userCreatePos();
        Assertions.assertThrows(Exception.class, ()-> userDataAccess.createUser(new UserData("hey", "yo", "yo@yo")));
    }
    @Test
    public void getUserPos()throws Exception {
        userCreatePos();
        UserData loggedUser =  userDataAccess.getUser("hey");
        Assertions.assertNotNull(loggedUser);
        Assertions.assertEquals("hey", loggedUser.username());
    }
    @Test
    public void getUserNeg()throws Exception {
        userCreatePos();
        Assertions.assertNull(userDataAccess.getUser("h"));
    }
    @Test
    public void authCreatePos() throws Exception {
        authDataAccess.createAuth(new AuthData("someAuth", "yo"));
    }
    @Test
    public void authCreateNeg() throws Exception {
        authCreatePos();
        Assertions.assertThrows(Exception.class, ()-> authDataAccess.createAuth(new AuthData("someAuth", "yo")));
    }
    @Test
    public void getAuthPos()throws Exception {
        authCreatePos();
        AuthData loggedUser =  authDataAccess.getAuthByToken("someAuth");
        Assertions.assertNotNull(loggedUser);
        Assertions.assertEquals("yo", loggedUser.username());
    }
    @Test
    public void getAuthNeg()throws Exception {
        authCreatePos();
        Assertions.assertNull(authDataAccess.getAuthByToken("h"));
    }
    @Test
    public void deleteAuthPos()throws Exception {
        authCreatePos();
        authDataAccess.deleteAuth(authDataAccess.getAuthByToken("someAuth"));
        Assertions.assertNull(authDataAccess.getAuthByToken("someAuth"));
    }
    @Test
    public void deleteAuthNeg()throws Exception {
        Assertions.assertThrows(Exception.class, () -> authDataAccess.deleteAuth(new AuthData("bogus", "user")));
    }
    @Test
    public void gameCreatePos() throws Exception {
        gameDataAccess.createGame(1, new GameData(1, "yo", null, "hey", new ChessGame()));
    }
    @Test
    public void gameCreateNeg() throws Exception {
        gameCreatePos();
        Assertions.assertThrows(Exception.class, ()->
                gameDataAccess.createGame(1, new GameData(1, null, null, null, new ChessGame())));
    }
    @Test
    public void getGamePos()throws Exception {
        gameCreatePos();
        GameData game =  gameDataAccess.getGame(1);
        Assertions.assertNotNull(game);
        Assertions.assertEquals("yo", game.whiteUsername());
    }
    @Test
    public void getGameNeg()throws Exception {
        gameCreatePos();
        Assertions.assertNull(gameDataAccess.getGame(5));
    }
    @Test
    public void updateGamePos()throws Exception {
        gameCreatePos();
        gameDataAccess.updateGame(1, new GameData(1, null, "hey", "five", new ChessGame()));
        GameData game = gameDataAccess.getGame(1);
        Assertions.assertNotNull(game);
        Assertions.assertEquals("hey", game.blackUsername());
        Assertions.assertNull(game.whiteUsername());
    }
    @Test
    public void updateGameNeg()throws Exception {
        Assertions.assertThrows(Exception.class, () ->
                gameDataAccess.updateGame(4, new GameData(1, null, "hey", "five", new ChessGame())));
    }
    @Test
    public void listGamesEmpty() throws Exception{
        gameDataAccess.listGames();
    }
    @Test
    public void listGamesPopulated() throws Exception{
        gameCreatePos();
        HashMap<Integer, GameData> list = gameDataAccess.listGames();
        Assertions.assertNotNull(list);
    }
}
