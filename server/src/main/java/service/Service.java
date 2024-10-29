package service;

import dataaccess.*;
import model.*;

import java.util.Objects;
import java.util.UUID;

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
        if(authDataAccess.getAuth(username) != null){
            throw new SecurityException("user already logged in");
        }
        if(!Objects.equals(logger.password(), password)){
            throw new SecurityException("unauthorized");
        }
        String authToken = generateAuthToken();
        return new AuthData(authToken, username);
    }
    public void logoutUser(String authToken){

    }
    /*
    logoutUser
        getAuth() //throw error if null
        deleteAuth()
        return result
     */
    /*
    listGames
        getAuth()
        getGames()
        list result
     */
    /*
    createGame
        getAuth()
        assignGameID
        createGame()
        return ID on success
     */
    /*
    joinGame
        getAuth()
        getGame(ID)
        verify there is space for player
        updateGame()
     */

}
