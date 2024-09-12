package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        PieceType piece = this.getPieceType();
        int rowHold = myPosition.getRow();
        int colHold = myPosition.getColumn();
        switch(piece){
            case KING:
                break;
            case QUEEN:
                break;
            case BISHOP:
                while ((rowHold < 8) && (colHold < 8)){
                    rowHold++;
                    colHold++;
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold, colHold), null));
}
                rowHold = myPosition.getRow();
                colHold = myPosition.getColumn();
                while ((rowHold > 1) && (colHold > 1)){
                    rowHold--;
                    colHold--;
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold, colHold), null));
}
                rowHold = myPosition.getRow();
                colHold = myPosition.getColumn();
                while ((rowHold < 8) && (colHold > 1)){
                    rowHold++;
                    colHold--;
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold, colHold), null));
}
                rowHold = myPosition.getRow();
                colHold = myPosition.getColumn();
                while ((rowHold > 1) && (colHold < 8)){
                    rowHold--;
                    colHold++;
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold, colHold), null));

                }
                break;
            case KNIGHT:
                break;
            case ROOK:
                break;
            case PAWN:
                break;
        }
        return moves;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
