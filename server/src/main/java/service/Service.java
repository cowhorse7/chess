package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;

public class Service {
    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;
    public int currentGameCount;

    public Service(UserDAO userDataAccess, AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
        currentGameCount = 0;
    }

    public void clear() throws Exception {
        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData registerUser(UserData newUser) throws Exception {
        if (userDataAccess.getUser(newUser.username()) != null) {
            throw new Exception("Error: already taken");
        }
        if (newUser.username() == null || newUser.password() == null || newUser.email() == null) {
            throw new Exception("Error: bad request");
        }
        userDataAccess.createUser(newUser);
        String authToken = generateAuthToken();
        AuthData registerAuth = new AuthData(authToken, newUser.username());
        authDataAccess.createAuth(registerAuth);
        return registerAuth;
    }

    public LoginResponse loginUser(String username, String password) throws Exception {
        if (userDataAccess.getUser(username) == null) {
            throw new Exception("Error: user does not exist");
        }
        UserData logger = userDataAccess.getUser(username);
        if (!BCrypt.checkpw(password, logger.password())){//Objects.equals(logger.password(), password)) {
            throw new Exception("Error: unauthorized");
        }
        String authToken = generateAuthToken();
        AuthData userAuth = new AuthData(authToken, username);
        authDataAccess.createAuth(userAuth);
        return new LoginResponse(username, authToken);
    }

    public void logoutUser(String authToken) throws Exception {
        AuthData userAuth = checkAuth(authToken);
        authDataAccess.deleteAuth(userAuth);
    }

    public AuthData checkAuth(String authToken) throws Exception {
        AuthData userAuth = authDataAccess.getAuthByToken(authToken);
        if (userAuth == null) {
            throw new Exception("Error: unauthorized");
        }
        return userAuth;
    }

    public ListGamesResponse listGames(String authToken) throws Exception {
        HashSet<GameData> listOfGames = new HashSet<>();
        checkAuth(authToken);
        HashMap<Integer, GameData> gameDatabase = gameDataAccess.listGames();
        gameDatabase.forEach(
                (key, value)
                        -> listOfGames.add(value));
        return new ListGamesResponse(listOfGames);
    }

    public int createGame(String authToken, String gameName) throws Exception {
        checkAuth(authToken);
        int gameID = currentGameCount + 1;
        currentGameCount += 1;
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDataAccess.createGame(gameID, newGame);
        return gameID;
    }

    public void joinGame(String authToken, String playerColor, Integer gameID) throws Exception {
        AuthData currentUser = checkAuth(authToken);
        if (playerColor == null || gameID == null) {
            throw new Exception("Error: bad request");
        }
        GameData game = gameDataAccess.getGame(gameID);
        if (game == null) {
            throw new Exception("Error: bad request");
        }//game does not exist
        if (Objects.equals(playerColor, "WHITE") && game.whiteUsername() != null) {
            throw new Exception("Error: already taken");
        }
        else if (Objects.equals(playerColor, "BLACK") && game.blackUsername() != null) {
            throw new Exception("Error: already taken");
        }
        else if (Objects.equals(playerColor, "BLACK") && game.blackUsername() == null) {
            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), currentUser.username(), game.gameName(), game.game());
            gameDataAccess.updateGame(gameID, updatedGame);
        } else if (Objects.equals(playerColor, "WHITE") && game.whiteUsername() == null) {
            GameData updatedGame = new GameData(game.gameID(), currentUser.username(), game.blackUsername(), game.gameName(), game.game());
            gameDataAccess.updateGame(gameID, updatedGame);
        }
    }

}
