package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    private boolean captureStop = false;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }
    public ChessPiece(ChessPiece p){
        this.pieceColor = p.pieceColor;
        this.type = p.type;
        this.captureStop = p.captureStop;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    private boolean spaceCheck(int row, int col){
        return row > 0 && row < 9 && col > 0 && col < 9;
    }

    private boolean pieceCheck(ChessBoard board, ChessPosition start, ChessPosition end){
        if(board.getPiece(start).type == PieceType.PAWN){
            if (board.getPiece(end) != null){
                return false;
            }
        }
        if (board.getPiece(end) != null){
            captureStop = true;
            return board.getPiece(end).pieceColor != board.getPiece(start).pieceColor;
        }
        return true;
    }
    private void determineMoveSet(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> moves){
        switch (type){
            case BISHOP:
                int[][] bishopMoveDirections = {{1,1},{1,-1},{-1,1},{-1,-1}};
                calculateMoves(board, myPosition, moves, bishopMoveDirections);
                break;
            case ROOK:
                int[][] rookMoveDirections = {{1,0},{0,-1},{0,1},{-1,0}};
                calculateMoves(board, myPosition, moves, rookMoveDirections);
                break;
            case QUEEN,KING:
                int[][] queenMoveDirections = {{1,1},{1,-1},{-1,1},{-1,-1},{1,0},{0,-1},{0,1},{-1,0}};
                calculateMoves(board, myPosition, moves, queenMoveDirections);
                break;
            case KNIGHT:
                int[][] knightMoveDirections = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,-2},{-1,2}};
                calculateMoves(board, myPosition, moves, knightMoveDirections);
        }
    }
    private void calculateMoves(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> moves, int[][] moveDirections){
        int curRow = myPosition.getRow();
        int curCol = myPosition.getColumn();
        for (int[] direction : moveDirections) {
            int rowDir = direction[0];
            int colDir = direction[1];
            int i = 1;
            captureStop = false;
            while (spaceCheck(curRow + (i * rowDir), curCol + (i * colDir)) && pieceCheck(board, myPosition, new ChessPosition(curRow + (i * rowDir), curCol + (i * colDir)))) {
                moves.add(new ChessMove(myPosition, new ChessPosition(curRow + (i * rowDir), curCol + (i * colDir)), null));
                if (captureStop || this.type == PieceType.KING || this.type == PieceType.KNIGHT){break;}
                i++;
            }
        }
    }
    private void pawnPromotionPossibilities(ChessPosition myPosition, ChessPosition endPosition, ArrayList<ChessMove> moves){
        moves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
        moves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
        moves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove>moveSet = new ArrayList<>();
        int i = 1;
        int curRow = myPosition.getRow();
        int curCol = myPosition.getColumn();
        switch (type) {
            case BISHOP, ROOK, QUEEN, KING, KNIGHT:
                determineMoveSet(board, myPosition, moveSet);
                break;
            case PAWN:
                if (pieceColor == ChessGame.TeamColor.WHITE){
                    if (spaceCheck(curRow + 1, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow + 1, curCol))) {
                        if (curRow + 1 == 8){
                            pawnPromotionPossibilities(myPosition, new ChessPosition(curRow + 1, curCol), moveSet);
                        }
                        else {moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol), null));}
                        if (curRow == 2 && spaceCheck(curRow + 2, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow + 2, curCol))){
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 2, curCol), null));
                        }
                    }
                    if (spaceCheck(curRow + 1, curCol + 1) && board.getPiece(new ChessPosition(curRow + 1, curCol + 1)) != null && board.getPiece(new ChessPosition(curRow + 1, curCol + 1)).pieceColor == ChessGame.TeamColor.BLACK) {
                        if (curRow + 1 == 8){
                            pawnPromotionPossibilities(myPosition, new ChessPosition(curRow + 1, curCol + 1), moveSet);
                        }
                        else {moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol + 1), null));}
                    }
                    if (spaceCheck(curRow + 1, curCol - 1) && board.getPiece(new ChessPosition(curRow + 1, curCol - 1)) != null && board.getPiece(new ChessPosition(curRow + 1, curCol - 1)).pieceColor == ChessGame.TeamColor.BLACK) {
                        if (curRow + 1 == 8){
                            pawnPromotionPossibilities(myPosition, new ChessPosition(curRow + 1, curCol - 1), moveSet);
                        }
                        else {moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol - 1), null));}
                    }

                }
                if (pieceColor == ChessGame.TeamColor.BLACK){
                    if (spaceCheck(curRow - 1, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow - 1, curCol))) {
                        if (curRow - 1 == 1){
                            pawnPromotionPossibilities(myPosition, new ChessPosition(curRow - 1, curCol), moveSet);
                        }
                        else {moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol), null));}
                        if (curRow == 7 && spaceCheck(curRow - 2, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow - 2, curCol))){
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 2, curCol), null));
                        }
                    }
                    if (spaceCheck(curRow - 1, curCol - 1) && board.getPiece(new ChessPosition(curRow - 1, curCol - 1)) != null && board.getPiece(new ChessPosition(curRow - 1, curCol - 1)).pieceColor == ChessGame.TeamColor.WHITE) {
                        if (curRow - 1 == 1){
                            pawnPromotionPossibilities(myPosition, new ChessPosition(curRow - 1, curCol - 1), moveSet);
                        }
                        else {moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol - 1), null));}
                    }
                    if (spaceCheck(curRow - 1, curCol + 1) && board.getPiece(new ChessPosition(curRow - 1, curCol + 1)) != null && board.getPiece(new ChessPosition(curRow - 1, curCol + 1)).pieceColor == ChessGame.TeamColor.WHITE) {
                        if (curRow - 1 == 1){
                            pawnPromotionPossibilities(myPosition, new ChessPosition(curRow - 1, curCol + 1), moveSet);
                        }
                        else {moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol + 1), null));}
                    }

                }
                break;
        }
        return moveSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
