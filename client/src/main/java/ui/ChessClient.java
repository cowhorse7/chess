package ui;

import chess.*;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import serverfacade.*;
import static ui.EscapeSequences.*;
import java.util.*;

public class ChessClient {
    private AuthData currentUser;
    private String username = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private PlayerPosition playerPosition;
    private final Gson serializer = new Gson();
    private final DrawBoard drawBoard = new DrawBoard();
    private ChessBoard chessBoard;
    private Integer gameNum;
    private final NotificationHandler notificationHandler;
    private final String serverUrl;
    private WebsocketFacade ws;
    public ChessClient(String serverUrl, NotificationHandler notificationHandler){
        this.serverUrl = serverUrl;
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
                case "move" -> makeMove(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);

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
        currentUser = auth;
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
        currentUser = auth;
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
        chessBoard = serializer.fromJson(server.getGameBoardString(gameNum), ChessBoard.class);
        server.joinGame(gameNum, params[1]);
        state = State.INGAME;
        this.gameNum = gameNum;
        if(Objects.equals(params[1], "white")){
            playerPosition = PlayerPosition.WHITE;
            ws = new WebsocketFacade(serverUrl, notificationHandler);
            ws.enterGame(currentUser.authToken(), server.getGameID(gameNum));
            return "Now playing as white";//drawBoard.gameBoardWhite(chessBoard, null);
        }
        else{
            playerPosition = PlayerPosition.BLACK;
            ws = new WebsocketFacade(serverUrl, notificationHandler);
            ws.enterGame(currentUser.authToken(), server.getGameID(gameNum));
            return "Now playing as black";// drawBoard.gameBoardBlack(chessBoard, null);
        }
    }
    public String observeGame(String... params) throws Exception {
        if (params.length != 1) {
            return "You must include <gameNumber>";
        }
        assertLoggedIn();
        int gameNum = Integer.parseInt(params[0]);
        this.gameNum = gameNum;
        chessBoard = serializer.fromJson(server.getGameBoardString(gameNum), ChessBoard.class);
        state = State.INGAME;
        playerPosition = PlayerPosition.OBSERVER;
        ws = new WebsocketFacade(serverUrl, notificationHandler);
        ws.enterGame(currentUser.authToken(), server.getGameID(gameNum));
        return String.format("Now observing game %d", gameNum);//drawBoard.gameBoard(chessBoard, null);
    }
    public void setBoard(ChessGame game) throws Exception {
        this.chessBoard = game.cBoard;
        server.setGame(gameNum, game);
    }
    public String redraw() throws Exception {
        assertLoggedIn();
        assertInGame();
        if(playerPosition == PlayerPosition.WHITE) {
            return drawBoard.gameBoardWhite(chessBoard, null);
        } else if (playerPosition == PlayerPosition.BLACK) {
            return drawBoard.gameBoardBlack(chessBoard, null);
        }
        else {return drawBoard.gameBoard(chessBoard, null);}
    }
    public String leave() throws Exception {
        assertLoggedIn();
        assertInGame();
        ws.leaveGame(currentUser.authToken(), server.getGameID(gameNum));
        state = State.SIGNEDIN;
        return  "Successfully left game.\nType \"help\" for options";
    }
    public String makeMove(String... params) throws Exception {
        if (params.length != 2 && params.length != 3) {
            return "Please format request: <currentColumnLetter><currentRowNumber> <endColumnLetter><endRowNumber>";
        }
        assertLoggedIn();
        assertInGame();

        int startRow = Integer.parseInt(String.valueOf(params[0].charAt(1)));
        char startColumn = params[0].charAt(0);
        int sCol = extractColumn(startColumn);
        int endRow = Integer.parseInt(String.valueOf(params[1].charAt(1)));
        char endColumn = params[1].charAt(0);
        int eCol = extractColumn(endColumn);

        ChessPosition start = new ChessPosition(startRow, sCol);
        ChessPosition end = new ChessPosition(endRow, eCol);
        ChessGame game = server.getGame(gameNum);
        ChessPiece.PieceType promPiece = promotionPieceCheck(game, start, end);
        ChessMove move;
        if(promPiece== null){move =new ChessMove(start, end, null);}
        else{move = new ChessMove(start, end, promPiece);}

        ws.makeMove(currentUser.authToken(), server.getGameID(gameNum), move);
        return "";
    }
    public ChessPiece.PieceType promotionPieceCheck(ChessGame game, ChessPosition start, ChessPosition end) throws Exception{
        String[] validTypes = {"bishop", "queen", "rook", "knight"};
        ChessPiece piece = game.cBoard.getPiece(start);
        if(piece == null){throw new Exception("No piece to move");}
        else if (piece.getPieceType() != ChessPiece.PieceType.PAWN){return null;}
        else{
            Scanner scanner = new Scanner(System.in);
            if(piece.getTeamColor() == ChessGame.TeamColor.BLACK && end.getRow() == 1
                    || piece.getTeamColor() == ChessGame.TeamColor.WHITE && end.getRow() == 8) {
                System.out.print("Please input the type of piece to which you would like to promote your pawn:\n");
                String line = scanner.nextLine();
                line = line.toLowerCase();
                boolean contains = Arrays.asList(validTypes).contains(line);
                ChessPiece.PieceType promPiece = null;
                while (!contains) {
                    System.out.print("Please check your spelling. The available types are:\n");
                    System.out.print(String.join(", ", validTypes) + "\n");
                    line = scanner.nextLine();
                    line = line.toLowerCase();
                    contains = Arrays.asList(validTypes).contains(line);
                }
                switch (line) {
                    case "bishop" -> promPiece = ChessPiece.PieceType.BISHOP;
                    case "queen" -> promPiece = ChessPiece.PieceType.QUEEN;
                    case "rook" -> promPiece = ChessPiece.PieceType.ROOK;
                    case "knight" -> promPiece = ChessPiece.PieceType.KNIGHT;
                }
                return promPiece;
            }
        }
        return null;
    }
    public String resign() throws Exception {
        assertLoggedIn();
        assertInGame();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to resign? This will end the game! (y/n)\n");
        String line = scanner.nextLine();
        if(Objects.equals(line, "y")){
            ws.resignGame(currentUser.authToken(), server.getGameID(gameNum));
        } else if (Objects.equals(line, "n")) {return "You have not resigned";}
        else{return "Please type either \"y\" or \"n\"";}
        return "";
    }
    public String highlight(String... params) throws Exception {
        if (params.length != 1) {
            return "Please format request: <columnLetter><rowNumber>";
        }
        assertLoggedIn();
        assertInGame();
        int row = Integer.parseInt(String.valueOf(params[0].charAt(1)));
        char column = params[0].charAt(0);
        int col = extractColumn(column);
        return drawBoard.highlightLegalMoves(chessBoard, row, col, playerPosition);
    }

    public int extractColumn(char colLetter) throws Exception {
        char[] letters = {' ','a','b','c','d','e','f','g','h'};
        int col = new String(letters).indexOf(colLetter);
        if (col == -1){throw new Exception("Invalid column");}
        return col;
    }

}
