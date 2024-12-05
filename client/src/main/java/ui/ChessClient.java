package ui;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import serverfacade.ServerFacade;
import static ui.EscapeSequences.*;
import java.util.*;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private final Gson serializer = new Gson();
    private DrawBoard drawBoard = new DrawBoard();
    public ChessClient(String serverUrl){
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
    }
    public String eval(String input){
        try{
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch(cmd){
                case "login" -> login(params);
                case "logout" -> logout();
                case "register" -> register(params);
                case "quit" -> "quit";
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> playGame(params);
                case "observe" -> observeGame(params);
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> makeMove();
                case "resign" -> resign();
                case "highlight" -> highlight();

                default -> help();
            };
        }catch(Exception e){
            return SET_TEXT_COLOR_RED + e.getMessage();
        }
    }
    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - help
                    - register <username> <password> <email>
                    - login <username> <password>
                    - quit
                    """;
        } else if (state == State.INGAME) {
            return """
                    - help
                    - redraw -> show the current board
                    - leave -> exit game
                    - move <pieceLocation> <placeToMove> -> move a piece on the board
                    - resign -> forfeit or end game
                    - highlight <pieceLocation> -> show legal moves for a piece
                    """;
        } else {
            return """
                    - help
                    - list -> list existing games
                    - create <nameOfGame> -> create new game
                    - join <gameNumber> <colorYouWishToPlay> -> play an existing game
                    - observe <gameNumber> -> watch an existing game
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
        return server.listGames();
    }
    public String createGame(String... params) throws Exception {
        if (params.length!=1){return "Please include <nameOfGame> (no spaces allowed in name)";}
        assertLoggedIn();
        server.createChessGame(params[0]);
        return String.format("%s successfully created", params[0]);
    }
    public String playGame(String... params) throws Exception {
        if (params.length != 2) {
            return "You must include <gameNumber> <colorYouWishToPlay>";
        }
        assertLoggedIn();
        int gameNum = Integer.parseInt(params[0]);
        ChessBoard chessBoard = serializer.fromJson(server.getGame(gameNum), ChessBoard.class);
        server.joinGame(gameNum, params[1]);
        state = State.INGAME;
        return drawBoard.gameBoard(chessBoard);
    }
    public String observeGame(String... params) throws Exception {
        if (params.length != 1) {
            return "You must include <gameNumber>";
        }
        assertLoggedIn();
        int gameNum = Integer.parseInt(params[0]);
        ChessBoard chessBoard = serializer.fromJson(server.getGame(gameNum), ChessBoard.class);
        state = State.INGAME;
        return drawBoard.gameBoard(chessBoard);
    }
    public String redraw(){
        return null;
    }
    public String leave(){
        state = State.SIGNEDIN;
        return null;
    }
    public String makeMove(){
        return null;
    }
    public String resign(){
        return null;
    }
    public String highlight(){
        return null;
    }
}
