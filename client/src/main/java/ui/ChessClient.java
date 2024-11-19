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
        }else {
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
        if (params.length!=1){return "Please include <nameOfGame>";}
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
        return gameBoard(chessBoard);
    }
    public String observeGame(String... params) throws Exception {
        if (params.length != 1) {
            return "You must include <gameNumber>";
        }
        assertLoggedIn();
        int gameNum = Integer.parseInt(params[0]);
        ChessBoard chessBoard = serializer.fromJson(server.getGame(gameNum), ChessBoard.class);
        return gameBoard(chessBoard);
    }
    public String gameBoard(ChessBoard chessBoard){
        String[][] arr = new String[9][9];
        initGameBoard(arr, chessBoard);
        StringBuilder printBoards = new StringBuilder();
        printBoards.append(prettyBoard(arr));
        reverseBoard(arr);
        printBoards.append("\n");
        printBoards.append(prettyBoard(arr));
        return printBoards.toString();
    }
    public void reverseBoard(String[][]arr){
        reverseRows(arr);
        reverseColumns(arr);
    }
    public void reverseRows(String[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 1; j <= arr[i].length / 2; j++) {
                String temp = arr[i][j];
                arr[i][j] = arr[i][arr[i].length - j];
                arr[i][arr[i].length - j] = temp;
            }
        }
    }
    public static void reverseColumns(String[][] arr) {
        for (int j = 0; j < arr[0].length; j++) {
            for (int i = 1; i <= arr.length / 2; i++) {
                String temp = arr[i][j];
                arr[i][j] = arr[arr.length - i][j];
                arr[arr.length - i][j] = temp;
            }
        }
    }
    public String prettyBoard(String[][] arr){
        StringBuilder pretty = new StringBuilder();
        for(String[] row : arr){
            for (int i = 0; i < row.length; i++){
                pretty.append(row[i]);
            }
            pretty.append(RESET_BG_COLOR + "\n");
        }
        return pretty.toString();
    }
    public void initGameBoard(String[][] arr, ChessBoard chessBoard){
        char[] letters = {'0','a','b','c','d','e','f','g','h'};
        char[] nums = {'0', '8', '7', '6', '5', '4', '3', '2', '1'};
        String space = "";
        ChessPiece piece = null;
        for (int i = 0; i < 9; i ++){
            for(int j = 0; j < 9; j++){
                if (i == 0){
                    arr[i][j] = SET_TEXT_COLOR_GREEN + String.format(" %s ", letters[j]);
                }
                else if (j == 0){
                    arr[i][j] = SET_TEXT_COLOR_GREEN + RESET_BG_COLOR + String.format(" %s ", nums[i]);
                }
                else {
                    piece = chessBoard.getPiece(new ChessPosition(i, j));
                    if (piece == null){space = "   ";}
                    else{space = setSpace(piece, i);}

                    if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0)) {
                        arr[i][j] = SET_BG_COLOR_BLACK + SET_TEXT_COLOR_LIGHT_GREY + space;
                    } else {
                        arr[i][j] = SET_BG_COLOR_WHITE + SET_TEXT_COLOR_DARK_GREY + space;
                    }
                }
            }
        }
    }
    public String setSpace(ChessPiece piece, int definingLine){
        if(definingLine < 5) {
            switch (piece.getPieceType()) {
                case PAWN -> {return " p ";}
                case KNIGHT ->{ return " n ";}
                case KING -> {return " k ";}
                case QUEEN -> {return " q ";}
                case ROOK -> {return " r ";}
                case BISHOP -> {return " b ";}
            }
        }
        else{
            switch (piece.getPieceType()) {
                case PAWN -> {return " P ";}
                case KNIGHT -> {return " N ";}
                case KING -> {return " K ";}
                case QUEEN -> {return " Q ";}
                case ROOK -> {return " R ";}
                case BISHOP -> {return " B ";}
            }
        }
        return "   ";
    }
}
