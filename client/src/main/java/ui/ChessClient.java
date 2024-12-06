package ui;

import chess.ChessBoard;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import serverfacade.*;
import static ui.EscapeSequences.*;
import java.util.*;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private PlayerPosition playerPosition;
    private final Gson serializer = new Gson();
    private final DrawBoard drawBoard = new DrawBoard();
    private ChessBoard chessBoard;
    private Integer gameNum;
    private NotificationHandler notificationHandler;
    private WebsocketFacade ws;
    public ChessClient(String serverUrl, NotificationHandler notificationHandler){
        server = new ServerFacade(serverUrl);
        this.notificationHandler = notificationHandler;
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
    private void assertInGame() throws Exception {
        if (state != State.INGAME) {
            throw new Exception("You must join a game first");
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
        chessBoard = serializer.fromJson(server.getGame(gameNum), ChessBoard.class);
        server.joinGame(gameNum, params[1]);
        state = State.INGAME;
        this.gameNum = gameNum;
        if(Objects.equals(params[1], "white")){
            playerPosition = PlayerPosition.WHITE;
            return drawBoard.gameBoardWhite(chessBoard);
            //FIXME: ws.joinGameWhite
        }
        else{
            playerPosition = PlayerPosition.BLACK;
            return drawBoard.gameBoardBlack(chessBoard);
            //FIXME: ws.joinGameBlack
        }
    }
    public String observeGame(String... params) throws Exception {
        if (params.length != 1) {
            return "You must include <gameNumber>";
        }
        assertLoggedIn();
        int gameNum = Integer.parseInt(params[0]);
        chessBoard = serializer.fromJson(server.getGame(gameNum), ChessBoard.class);
        state = State.INGAME;
        playerPosition = PlayerPosition.OBSERVER;
        //FIXME: ws.observeGame
        this.gameNum = gameNum;
        return drawBoard.gameBoard(chessBoard);
    }
    public String redraw() throws Exception {
        assertLoggedIn();
        assertInGame();
        if(playerPosition == PlayerPosition.WHITE) {
            return drawBoard.gameBoardWhite(chessBoard);
        } else if (playerPosition == PlayerPosition.BLACK) {
            return drawBoard.gameBoardBlack(chessBoard);
        }
        else {return drawBoard.gameBoard(chessBoard);}
    }
    public String leave() throws Exception {
        assertLoggedIn();
        assertInGame();
        state = State.SIGNEDIN;
        server.leaveGame(gameNum);
        //FIXME: ws.leaveGame
        return  "Successfully left game.\nType \"help\" for options";
    }
    public String makeMove() throws Exception {
        assertLoggedIn();
        assertInGame();
        //FIXME: ws.makeMove
        return null;
    }
    public String resign() throws Exception {
        assertLoggedIn();
        assertInGame();
        //FIXME: ws.resign
        return null;
    }
    public String highlight(String... params) throws Exception {
        if (params.length != 2) {
            return "Please format request: <rowNumber> <columnLetter>";
        }
        assertLoggedIn();
        assertInGame();
        int row = Integer.parseInt(params[0]);
        char column = params[1].charAt(0);
        return drawBoard.highlightLegalMoves(chessBoard, row, column, playerPosition);
    }

}
