package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_DARK_GREY;

public class DrawBoard {
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
    public String gameBoardWhite(ChessBoard chessBoard){
        String[][] arr = new String[9][9];
        initGameBoard(arr, chessBoard);
        return STR."\{prettyBoard(arr)}\n";
    }
    public String gameBoardBlack(ChessBoard chessBoard){
        String[][] arr = new String[9][9];
        initGameBoard(arr, chessBoard);
        reverseBoard(arr);
        return STR."\{prettyBoard(arr)}\n";
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
        char[] letters = {' ','a','b','c','d','e','f','g','h'};
        char[] nums = {' ', '8', '7', '6', '5', '4', '3', '2', '1'};
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
                    else{space = setSpace(piece, piece.getTeamColor());}

                    if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0)) {
                        arr[i][j] = SET_BG_COLOR_BLACK + space; // + SET_TEXT_COLOR_LIGHT_GREY
                    } else {
                        arr[i][j] = SET_BG_COLOR_WHITE + space; //+ SET_TEXT_COLOR_DARK_GREY
                    }
                }
            }
        }
    }
    public String setSpace(ChessPiece piece, ChessGame.TeamColor color){
        String returnString = "";
        if(color == ChessGame.TeamColor.BLACK) {
            returnString = SET_TEXT_COLOR_MAGENTA;
        }
        else{returnString = SET_TEXT_COLOR_BLUE;}
        switch (piece.getPieceType()) {
                case PAWN -> {returnString += " P ";}
                case KNIGHT -> {returnString += " N ";}
                case KING -> {returnString += " K ";}
                case QUEEN -> {returnString += " Q ";}
                case ROOK -> {returnString += " R ";}
                case BISHOP -> {returnString += " B ";}
        }
        return returnString;
    }
}
