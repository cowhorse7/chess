package ui;
import serverfacade.ServerFacade;

import java.util.Arrays;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }
    public String eval(String input){
        try{
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd){
                case "login" -> login(params);
                case "logout" -> logout();
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
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            username = String.join("-", params);
            return String.format("You signed in as %s.", username);
        }
        throw new Exception("Expected: <username> <password>");
    }
    public String logout() throws Exception {
        assertLoggedIn();
        state = State.SIGNEDOUT;
        return String.format("%s logged out", username);
    }
    private void assertLoggedIn() throws Exception {
        if (state == State.SIGNEDOUT) {
            throw new Exception("You must sign in");
        }
    }
    public String register(){
        return"";
    }
    public String listGames(){
        return "";
    }
    public String createGame(){
        return "";
    }
    public String playGame(){
        return "";
    }
    public String observeGame(){
        return "";
    }
}
