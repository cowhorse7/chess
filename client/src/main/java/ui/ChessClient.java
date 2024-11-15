package ui;
import serverfacade.ServerFacade;
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
        return "";
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
}
