package service;

import dataaccess.*;
import model.*;

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
    public void clear(){}

    public UserData registerUser(UserData newUser) throws ServiceException {
        //do I need to de-serialize newUser here?
        if (userDataAccess.getUser(newUser) != null) {
            throw new SecurityException("User already exists");
        }
        else {
            userDataAccess.createUser(newUser);
            authDataAccess.createAuth(newUser);
        }
        return newUser;
    }

    /*
    loginUser
        getUser()
        validatePassword()
        return result (error if user not found in db)
     */
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
    /*
    clear
        clear(Auth)
        clear(User)
        clear(Game)
     */
}
