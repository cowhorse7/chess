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

    public boolean spaceCheck(int row, int col){
        if (row > 0 && row < 9 && col > 0 && col < 9){
            return true;
        }
        return false;
    }

    public boolean pieceCheck(ChessBoard board, ChessPosition start, ChessPosition end){
        if(board.getPiece(start).type == PieceType.PAWN){
            if (board.getPiece(end) != null){
                return false;
            }
        }
        if (board.getPiece(end) != null){
            captureStop = true;
            if (board.getPiece(end).pieceColor == board.getPiece(start).pieceColor){
                return false;
            }
        }
        return true;
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
            case KING:
                if (spaceCheck(curRow + 1, curCol + 1) && pieceCheck(board, myPosition, new ChessPosition(curRow+1, curCol + 1))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow+1, curCol + 1), null));
                }
                if (spaceCheck(curRow + 1, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow+1, curCol))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow+1, curCol), null));
                }
                if (spaceCheck(curRow + 1, curCol - 1) && pieceCheck(board, myPosition, new ChessPosition(curRow+1, curCol - 1))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow+1, curCol - 1), null));
                }
                if (spaceCheck(curRow - 1, curCol + 1) && pieceCheck(board, myPosition, new ChessPosition(curRow-1, curCol + 1))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow-1, curCol + 1), null));
                }
                if (spaceCheck(curRow - 1, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow-1, curCol))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow-1, curCol), null));
                }
                if (spaceCheck(curRow - 1, curCol - 1) && pieceCheck(board, myPosition, new ChessPosition(curRow-1, curCol - 1))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow-1, curCol - 1), null));
                }
                if (spaceCheck(curRow, curCol + 1) && pieceCheck(board, myPosition, new ChessPosition(curRow, curCol + 1))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow, curCol + 1), null));
                }
                if (spaceCheck(curRow, curCol - 1) && pieceCheck(board, myPosition, new ChessPosition(curRow, curCol - 1))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow, curCol - 1), null));
                }
                break;
            case QUEEN:
                i = 1;
                captureStop = false;
                while (spaceCheck(curRow + i, curCol + i) && pieceCheck(board, myPosition, new ChessPosition(curRow + i, curCol + i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + i, curCol + i), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow - i, curCol - i) && pieceCheck(board, myPosition, new ChessPosition(curRow - i, curCol - i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - i, curCol - i), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow + i, curCol - i) && pieceCheck(board, myPosition, new ChessPosition(curRow + i, curCol - i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + i, curCol - i), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow - i, curCol + i) && pieceCheck(board, myPosition, new ChessPosition(curRow - i, curCol + i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - i, curCol + i), null));
                    if (captureStop) break;
                    i++;
                }
                i = 1;
                captureStop = false;
                while (spaceCheck(curRow + i, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow + i, curCol))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + i, curCol), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow - i, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow - i, curCol))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - i, curCol), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow, curCol - i) && pieceCheck(board, myPosition, new ChessPosition(curRow, curCol - i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow, curCol - i), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow, curCol + i) && pieceCheck(board, myPosition, new ChessPosition(curRow, curCol + i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow, curCol + i), null));
                    if (captureStop) break;
                    i++;
                }
                break;
            case BISHOP:
                i = 1;
                captureStop = false;
                while (spaceCheck(curRow + i, curCol + i) && pieceCheck(board, myPosition, new ChessPosition(curRow + i, curCol + i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + i, curCol + i), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow - i, curCol - i) && pieceCheck(board, myPosition, new ChessPosition(curRow - i, curCol - i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - i, curCol - i), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow + i, curCol - i) && pieceCheck(board, myPosition, new ChessPosition(curRow + i, curCol - i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + i, curCol - i), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow - i, curCol + i) && pieceCheck(board, myPosition, new ChessPosition(curRow - i, curCol + i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - i, curCol + i), null));
                    if (captureStop) break;
                    i++;
                }
                break;
            case KNIGHT:
                if (spaceCheck(curRow + 2, curCol - 1) && pieceCheck(board, myPosition, new ChessPosition(curRow + 2, curCol - 1))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 2, curCol - 1), null));
                }
                if (spaceCheck(curRow + 2, curCol + 1) && pieceCheck(board, myPosition, new ChessPosition(curRow + 2, curCol + 1))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 2, curCol + 1), null));
                }
                if (spaceCheck(curRow - 2, curCol - 1) && pieceCheck(board, myPosition, new ChessPosition(curRow - 2, curCol - 1))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 2, curCol - 1), null));
                }
                if (spaceCheck(curRow - 2, curCol + 1) && pieceCheck(board, myPosition, new ChessPosition(curRow - 2, curCol + 1))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 2, curCol + 1), null));
                }
                if (spaceCheck(curRow - 1, curCol - 2) && pieceCheck(board, myPosition, new ChessPosition(curRow - 1, curCol - 2))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol - 2), null));
                }
                if (spaceCheck(curRow + 1, curCol - 2) && pieceCheck(board, myPosition, new ChessPosition(curRow + 1, curCol - 2))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol - 2), null));
                }
                if (spaceCheck(curRow - 1, curCol + 2) && pieceCheck(board, myPosition, new ChessPosition(curRow - 1, curCol + 2))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol + 2), null));
                }
                if (spaceCheck(curRow + 1, curCol + 2) && pieceCheck(board, myPosition, new ChessPosition(curRow + 1, curCol + 2))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol + 2), null));
                }
                break;
            case ROOK:
                i = 1;
                captureStop = false;
                while (spaceCheck(curRow + i, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow + i, curCol))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + i, curCol), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow - i, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow - i, curCol))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - i, curCol), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow, curCol - i) && pieceCheck(board, myPosition, new ChessPosition(curRow, curCol - i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow, curCol - i), null));
                    if (captureStop) break;
                    i++;
                }
                captureStop = false;
                i = 1;
                while (spaceCheck(curRow, curCol + i) && pieceCheck(board, myPosition, new ChessPosition(curRow, curCol + i))){
                    moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow, curCol + i), null));
                    if (captureStop) break;
                    i++;
                }
                break;
            case PAWN:
                if (pieceColor == ChessGame.TeamColor.WHITE){
                    if (spaceCheck(curRow + 1, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow + 1, curCol))) {
                        if (curRow + 1 == 8){
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol), PieceType.ROOK));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol), PieceType.KNIGHT));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol), PieceType.BISHOP));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol), PieceType.QUEEN));
                        }
                        else moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol), null));
                        if (curRow == 2 && spaceCheck(curRow + 2, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow + 2, curCol))){
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 2, curCol), null));
                        }
                    }
                    if (spaceCheck(curRow + 1, curCol + 1) && board.getPiece(new ChessPosition(curRow + 1, curCol + 1)) != null && board.getPiece(new ChessPosition(curRow + 1, curCol + 1)).pieceColor == ChessGame.TeamColor.BLACK) {
                        if (curRow + 1 == 8){
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol + 1), PieceType.ROOK));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol + 1), PieceType.KNIGHT));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol + 1), PieceType.BISHOP));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol + 1), PieceType.QUEEN));
                        }
                        else moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol + 1), null));
                    }
                    if (spaceCheck(curRow + 1, curCol - 1) && board.getPiece(new ChessPosition(curRow + 1, curCol - 1)) != null && board.getPiece(new ChessPosition(curRow + 1, curCol - 1)).pieceColor == ChessGame.TeamColor.BLACK) {
                        if (curRow + 1 == 8){
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol - 1), PieceType.ROOK));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol - 1), PieceType.KNIGHT));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol - 1), PieceType.BISHOP));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol - 1), PieceType.QUEEN));
                        }
                        else moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow + 1, curCol - 1), null));
                    }

                }
                if (pieceColor == ChessGame.TeamColor.BLACK){
                    if (spaceCheck(curRow - 1, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow - 1, curCol))) {
                        if (curRow - 1 == 1){
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol), PieceType.ROOK));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol), PieceType.KNIGHT));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol), PieceType.BISHOP));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol), PieceType.QUEEN));
                        }
                        else moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol), null));
                        if (curRow == 7 && spaceCheck(curRow - 2, curCol) && pieceCheck(board, myPosition, new ChessPosition(curRow - 2, curCol))){
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 2, curCol), null));
                        }
                    }
                    if (spaceCheck(curRow - 1, curCol - 1) && board.getPiece(new ChessPosition(curRow - 1, curCol - 1)) != null && board.getPiece(new ChessPosition(curRow - 1, curCol - 1)).pieceColor == ChessGame.TeamColor.WHITE) {
                        if (curRow - 1 == 1){
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol - 1), PieceType.ROOK));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol - 1), PieceType.KNIGHT));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol - 1), PieceType.BISHOP));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol - 1), PieceType.QUEEN));
                        }
                        else moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol - 1), null));
                    }
                    if (spaceCheck(curRow - 1, curCol + 1) && board.getPiece(new ChessPosition(curRow - 1, curCol + 1)) != null && board.getPiece(new ChessPosition(curRow - 1, curCol + 1)).pieceColor == ChessGame.TeamColor.WHITE) {
                        if (curRow - 1 == 1){
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol + 1), PieceType.ROOK));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol + 1), PieceType.KNIGHT));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol + 1), PieceType.BISHOP));
                            moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol + 1), PieceType.QUEEN));
                        }
                        else moveSet.add(new ChessMove(myPosition, new ChessPosition(curRow - 1, curCol + 1), null));
                    }

                }
                break;
        }
        return moveSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
