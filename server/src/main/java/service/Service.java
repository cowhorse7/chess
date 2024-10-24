package service;

import dataaccess.*;

public class Service {
    private final DataAccess dataAccess;
    public Service(DataAccess dataAccess) {this.dataAccess = dataAccess;}

    public UserData registerUser(UserData newUser) throws ServiceException {
        if (dataAccess.getUser(newUser.username()) != null) {
            throw new SecurityException("User already exists");
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
