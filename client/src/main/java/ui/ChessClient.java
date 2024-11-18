package ui;
import model.AuthData;
import model.UserData;
import serverfacade.ServerFacade;

import java.util.Arrays;

public class ChessClient {
    private String username = null;
    private String authToken = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
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
                    - listGames
                    - createGame
                    - playGame
                    - observeGame
                    - logout
                    - quit
                    """;
        }
    }
    public String login(String... params) throws Exception {
        if (params.length < 2) {
            return "You must include <username> <password>";
        }
        UserData user = new UserData(params[0], params[1], null);
        AuthData auth = server.loginUser(user);
        state = State.SIGNEDIN;
        authToken = auth.authToken();
        username = auth.username();
        return String.format("Successfully signed in as %s.\nType \"help\" for options", username);
    }
    public String logout() throws Exception {
        assertLoggedIn();
        server.logoutUser(authToken);
        state = State.SIGNEDOUT;
        return "Successfully logged out.\nType \"help\" for options";
    }
    private void assertLoggedIn() throws Exception {
        if (state == State.SIGNEDOUT) {
            throw new Exception("You must sign in");
        }
    }
    public String register(String... params) throws Exception {
        if (params.length < 3){
            return "You must include <username> <password> <email>";
        }
        UserData newUser = new UserData(params[0], params[1], params[2]);
        AuthData auth = server.createUser(newUser);
        authToken = auth.authToken();
        username = auth.username();
        state = State.SIGNEDIN;
        return String.format("Successfully registered as %s\nType \"help\" for options", username);
    }
    public String listGames() throws Exception {
        assertLoggedIn();
        return "";
    }
    public String createGame() throws Exception {
        assertLoggedIn();
        return "";
    }
    public String playGame() throws Exception {
        assertLoggedIn();
        return "";
    }
    public String observeGame() throws Exception {
        assertLoggedIn();
        return "";
    }
}
