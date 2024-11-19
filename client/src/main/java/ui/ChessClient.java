package ui;
import chess.ChessBoard;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import serverfacade.ListGamesResponse;
import serverfacade.ServerFacade;

import java.util.*;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private final Gson serializer = new Gson();
    public ChessClient(String serverUrl){
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
    }
    public String eval(String input){
        try{
            var tokens = input.toLowerCase().split(" "); //may want to change case sensitivity
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd){
                case "login" -> login(params);
                case "logout" -> logout();
                case "register" -> register(params);
                case "quit" -> "quit";
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join", "observe" -> playGame(params);
                //case "observe" -> observeGame(params);

                default -> help();
            };
        }catch(Exception e){
            return e.getMessage();
        }
    }
    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - help
                    - register
                    - login
                    - quit
                    """;
        }else {
            return """
                    - help
                    - list -> list existing games
                    - create -> create new game
                    - join -> play an existing game
                    - observe -> observe an existing game
                    - logout
                    - quit
                    """;
        }
    }
    public String login(String... params) throws Exception {
        if (state == State.SIGNEDIN){return "You are already signed in!";}
        if (params.length != 2) {
            return "You must include <username> <password>";
        }
        UserData user = new UserData(params[0], params[1], null);
        AuthData auth = server.loginUser(user);
        state = State.SIGNEDIN;
        username = auth.username();
        return String.format("Successfully signed in as %s.\nType \"help\" for options", username);
    }
    public String logout() throws Exception {
        assertLoggedIn();
        server.logoutUser();
        username = null;
        state = State.SIGNEDOUT;
        return "Successfully logged out.\nType \"help\" for options";
    }
    private void assertLoggedIn() throws Exception {
        if (state == State.SIGNEDOUT) {
            throw new Exception("You must sign in");
        }
    }
    public String register(String... params) throws Exception {
        if (state == State.SIGNEDIN){return "You are already signed in!";}
        if (params.length != 3){
            return "You must include <username> <password> <email>";
        }
        UserData newUser = new UserData(params[0], params[1], params[2]);
        AuthData auth = server.createUser(newUser);
        username = auth.username();
        state = State.SIGNEDIN;
        return String.format("Successfully registered as %s\nType \"help\" for options", username);
    }
    public String listGames() throws Exception {
        assertLoggedIn();
        return server.listGames(0);
    }
    public String createGame(String... params) throws Exception {
        if (params.length!=1){return "Please include <nameOfGame>";}
        assertLoggedIn();
        server.createChessGame(params[0]);
        return String.format("%s successfully created", params[0]);
    }
    public String playGame(String... params) throws Exception {
        if (params.length != 2) {
            return "You must include <gameID> <colorYouWishToPlay or \"observe\">";
        }
        assertLoggedIn();
        int gameID = Integer.parseInt(params[0]);
        String search = String.format("gameID: %d", gameID);
        String listOfGames = listGames();
        if(!listOfGames.contains(search)){
            return "Game does not exist";
        }
        String chessBoard = server.listGames(gameID);
        server.joinGame(gameID, params[1]);
        return gameBoard(chessBoard);
    }
//    public String observeGame(String... params) throws Exception {
//        if (params.length != 2) {
//            return "You must include <gameID> \"observe\"";
//        }
//        assertLoggedIn();
//        return "";
//    }
    public String gameBoard(String chessBoard){
        char[][] arr = new char[9][9];
        initGameBoard(arr);

        return "";
    }
    public void initGameBoard(char[][] arr){
        char[] letters = {'0','a','b','c','d','e','f','g','h'};
        int[] nums = {0, 8, 7, 6, 5, 4, 3, 2, 1};
        for (int i = 0; i < 9; i ++){
            for(int j = 0; j < 9; j++){
                if (i == 0){arr[i][j] = letters[j];}
                else if (j == 0){arr[i][j] = (char) nums[i];}
                else if ((i%2 == 0 && j%2 == 1) || (i%2 == 1 && j%2 == 0)){arr[i][j] = 'n';}
                else{arr[i][j] = 'w';}
            }
        }
    }
}
