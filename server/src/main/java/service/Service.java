package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;

import java.util.*;

public class Service {
    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public Service(UserDAO userDataAccess, AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;

    }
//    public RegisterResult register(RegisterRequest request){return null;}
//    public LoginResult login(LoginRequest request){return null;}
//    public void logout(LogoutRequest request){}
//    public void join(JoinGameRequest request){}
//    public CreateGameResult createGame(CreateGameRequest request){return null;}
//    public ListGamesResult list(ListGamesRequest request){return null;}
    public void clear(){
        userDataAccess.clear();
        authDataAccess.clear();
        gameDataAccess.clear();
    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
    public AuthData registerUser(UserData newUser) throws ServiceException {
        //do I need to de-serialize newUser here?
        if (userDataAccess.getUser(newUser.username()) != null) {
            throw new SecurityException("User already exists");
        }
        userDataAccess.createUser(newUser);
        String authToken = generateAuthToken();
        AuthData registerAuth = new AuthData(authToken, newUser.username());
        authDataAccess.createAuth(registerAuth);
        return registerAuth;
    }

    public AuthData loginUser(String username, String password) throws ServiceException {
        if(userDataAccess.getUser(username) == null){throw new ServiceException("user does not exist");}
        UserData logger = userDataAccess.getUser(username);
        if(authDataAccess.getAuthByUser(username) != null){
            throw new SecurityException("user already logged in");
        }
        if(!Objects.equals(logger.password(), password)){
            throw new SecurityException("unauthorized");
        }
        String authToken = generateAuthToken();
        AuthData userAuth = new AuthData(authToken, username);
        authDataAccess.createAuth(userAuth);
        return userAuth;
    }
    public void logoutUser(String authToken){
        AuthData userAuth = checkAuth(authToken);
        authDataAccess.deleteAuth(userAuth);
    }

    public AuthData checkAuth(String authToken){
        AuthData userAuth = authDataAccess.getAuthByToken(authToken);
        if (userAuth == null){
            throw new SecurityException("unauthorized");
        }
        return userAuth;
    }
    public ArrayList<GameData> listGames(String authToken){
        ArrayList<GameData> listOfGames = new ArrayList<>();
        checkAuth(authToken);
        HashMap<Integer, GameData>gameDatabase = gameDataAccess.listGames();
        gameDatabase.forEach(
                (key, value)
                    -> listOfGames.add(value));
        return listOfGames;
    }
    public int createGame(String authToken, String gameName){
        checkAuth(authToken);
        int gameID = gameDataAccess.getGameDatabaseSize() + 1;
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDataAccess.createGame(gameID, newGame);
        return gameID;
    }
    public void joinGame(String authToken, String playerColor, int gameID) throws ServiceException {
        checkAuth(authToken);
        GameData gameInQuestion = gameDataAccess.getGame(gameID);
        if(gameInQuestion == null){throw new ServiceException("game does not exist");}
        if(Objects.equals(playerColor, "black") && gameInQuestion.blackUsername() == null){
            GameData updatedGame = new GameData(gameInQuestion.gameID(), gameInQuestion.whiteUsername(), authDataAccess.getAuthByToken(authToken).username(), gameInQuestion.gameName(), gameInQuestion.game());
        }
        else if(Objects.equals(playerColor, "white") && gameInQuestion.whiteUsername() == null){
            GameData updatedGame = new GameData(gameInQuestion.gameID(), authDataAccess.getAuthByToken(authToken).username(), gameInQuestion.blackUsername(), gameInQuestion.gameName(), gameInQuestion.game());
        }

    }
    /*
    joinGame
        getAuth()
        getGame(ID)
        verify there is space for player
        updateGame()
     */

}
