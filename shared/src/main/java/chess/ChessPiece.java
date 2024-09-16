package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
//import java.util.Scanner;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private PieceType type;

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

    public boolean pieceCheck(ChessBoard board, ChessMove move){
        if (board.getPiece(move.getEndPosition())!= null) {
            if (board.getPiece(move.getStartPosition()).getPieceType()== PieceType.PAWN) {return false;}
            return board.getPiece(move.getStartPosition()).getTeamColor() != board.getPiece(move.getEndPosition()).getTeamColor();
        }
        return true;
    }

//    public PieceType uChange (){
//        var in = new Scanner(System.in);
//        System.out.print("Please choose your new piece: QUEEN, KNIGHT, BISHOP, or ROOK");
//        String usChange = in.next();
//        while (!Objects.equals(usChange, "QUEEN") && !Objects.equals(usChange, "KNIGHT") && !Objects.equals(usChange, "BISHOP") && !Objects.equals(usChange, "ROOK")){
//            System.out.print("Please choose one of the four options (type in caps)");
//            usChange = in.next();
//        }
//        return PieceType.valueOf(usChange);
//    }

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
                if (rowHold + 1 < 9 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold+1, colHold), null))) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold), null));
                }
                if (rowHold + 1 < 9 && colHold + 1 < 9 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold+1, colHold+1), null))) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold + 1), null));
                }
                if (rowHold + 1 < 9 && colHold - 1 > 0 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold+1, colHold-1), null))) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold - 1), null));
                }
                if (rowHold - 1 > 0 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold-1, colHold), null))) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold), null));
                }
                if (rowHold - 1 > 0 && colHold - 1 > 0 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold-1, colHold-1), null))) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold - 1), null));
                }
                if (rowHold - 1 > 0 && colHold + 1 < 9 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold-1, colHold+1), null))) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold + 1), null));
                }
                if (colHold + 1 < 8 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold, colHold+1), null))) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold, colHold + 1), null));
                }
                if (colHold - 1 > 0 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold, colHold-1), null))) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(rowHold, colHold - 1), null));
                }
                break;
            case QUEEN:
                break;
            case BISHOP:
                while ((rowHold < 8) && (colHold < 8)){
                    rowHold++;
                    colHold++;
                    ChessPosition holder = new ChessPosition(rowHold, colHold);
                    if (board.getPiece(holder)!= null){
                        if(board.getPiece(holder).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            moves.add(new ChessMove(myPosition, holder, null));
                        }
                        break;
                    }
                    moves.add(new ChessMove(myPosition, holder, null));
                }
                rowHold = myPosition.getRow();
                colHold = myPosition.getColumn();
                while ((rowHold > 1) && (colHold > 1)){
                    rowHold--;
                    colHold--;
                    ChessPosition holder = new ChessPosition(rowHold, colHold);
                    if (board.getPiece(holder)!= null){
                        if(board.getPiece(holder).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            moves.add(new ChessMove(myPosition, holder, null));
                        }
                        break;
                    }
                    moves.add(new ChessMove(myPosition, holder, null));
                }
                rowHold = myPosition.getRow();
                colHold = myPosition.getColumn();
                while ((rowHold < 8) && (colHold > 1)){
                    rowHold++;
                    colHold--;
                    ChessPosition holder = new ChessPosition(rowHold, colHold);
                    if (board.getPiece(holder)!= null){
                        if(board.getPiece(holder).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            moves.add(new ChessMove(myPosition, holder, null));
                        }
                        break;
                    }
                    moves.add(new ChessMove(myPosition, holder, null));
                }
                rowHold = myPosition.getRow();
                colHold = myPosition.getColumn();
                while ((rowHold > 1) && (colHold < 8)){
                    rowHold--;
                    colHold++;
                    ChessPosition holder = new ChessPosition(rowHold, colHold);
                    if (board.getPiece(holder)!= null){
                        if(board.getPiece(holder).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            moves.add(new ChessMove(myPosition, holder, null));
                        }
                        break;
                    }
                    moves.add(new ChessMove(myPosition, holder, null));
                }
                break;
            case KNIGHT:
                if(rowHold + 2 < 9){
                    if(colHold + 1 < 9 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold+2, colHold+1), null))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold+2, colHold+1), null));
                    }
                    if(colHold - 1 > 0 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold+2, colHold-1), null))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold+2, colHold-1), null));
                    }
                }
                if(rowHold - 2 > 0){
                    if(colHold + 1 < 9 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold-2, colHold+1), null))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold-2, colHold+1), null));
                    }
                    if(colHold - 1 > 0 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold-2, colHold-1), null))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold-2, colHold-1), null));
                    }
                }
                if(colHold + 2 < 9){ //adding the equal sign made the last test pass, which is either a fluke, or I need to add the = to all the other tests, too
                    if(rowHold + 1 < 9 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold+1, colHold+2), null))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold+1, colHold+2), null));
                    }
                    if(rowHold - 1 > 0 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold-1, colHold+2), null))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold-1, colHold+2), null));
                    }
                }
                if(colHold - 2 > 0){
                    if(rowHold + 1 < 9 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold+1, colHold-2), null))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold+1, colHold-2), null));
                    }
                    if(rowHold - 1 > 0 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold-1, colHold-2), null))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold-1, colHold-2), null));
                    }
                }
                break;
            case ROOK:
                while (rowHold < 8){
                    rowHold++;
                    ChessPosition holder = new ChessPosition(rowHold, colHold);
                    if (board.getPiece(holder)!= null){
                        if(board.getPiece(holder).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            moves.add(new ChessMove(myPosition, holder, null));
                        }
                        break;
                    }
                    moves.add(new ChessMove(myPosition, holder, null));
                }
                rowHold = myPosition.getRow();
                while (rowHold > 1){
                    rowHold--;
                    ChessPosition holder = new ChessPosition(rowHold, colHold);
                    if (board.getPiece(holder)!= null){
                        if(board.getPiece(holder).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            moves.add(new ChessMove(myPosition, holder, null));
                        }
                        break;
                    }
                    moves.add(new ChessMove(myPosition, holder, null));
                }
                rowHold = myPosition.getRow();
                while (colHold > 1){
                    colHold--;
                    ChessPosition holder = new ChessPosition(rowHold, colHold);
                    if (board.getPiece(holder)!= null){
                        if(board.getPiece(holder).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            moves.add(new ChessMove(myPosition, holder, null));
                        }
                        break;
                    }
                    moves.add(new ChessMove(myPosition, holder, null));
                }
                colHold = myPosition.getColumn();
                while (colHold < 8){
                    colHold++;
                    ChessPosition holder = new ChessPosition(rowHold, colHold);
                    if (board.getPiece(holder)!= null){
                        if(board.getPiece(holder).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                            moves.add(new ChessMove(myPosition, holder, null));
                        }
                        break;
                    }
                    moves.add(new ChessMove(myPosition, holder, null));
                }
                break;
            case PAWN:
                if(Objects.equals(board.getPiece(myPosition).getTeamColor().toString(), "WHITE")){
                    if(rowHold + 1 < 8 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold), null))) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold), null));
                        if (rowHold == 2 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold + 2, colHold), null))) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 2, colHold), null));
                        }
                    }
                    if(rowHold + 1 == 8 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold), null))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold), PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold), PieceType.KNIGHT));
                    }
                    if(rowHold + 1 < 9 && colHold + 1 < 9 && board.getPiece(new ChessPosition(rowHold + 1, colHold + 1)) != null && !Objects.equals(board.getPiece(new ChessPosition(rowHold + 1, colHold + 1)).getTeamColor().toString(), "WHITE")){
                        if(rowHold + 1 == 8) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold + 1), PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold + 1), PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold + 1), PieceType.KNIGHT));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold + 1), PieceType.ROOK));
                        }
                        else moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold + 1), null));
                    }
                    if(rowHold + 1 < 9 && colHold - 1 > 0 && board.getPiece(new ChessPosition(rowHold + 1, colHold - 1)) != null && !Objects.equals(board.getPiece(new ChessPosition(rowHold + 1, colHold - 1)).getTeamColor().toString(), "WHITE")){
                        if(rowHold + 1 == 8) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold - 1), PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold - 1), PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold - 1), PieceType.ROOK));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold - 1), PieceType.KNIGHT));
                        }
                        else moves.add(new ChessMove(myPosition, new ChessPosition(rowHold + 1, colHold - 1), null));
                    }
                }
                if(Objects.equals(board.getPiece(myPosition).getTeamColor().toString(), "BLACK")){
                    if(rowHold - 1 > 1 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold), null))) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold), null));
                        if (rowHold == 7 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold - 2, colHold), null))) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 2, colHold), null));
                        }
                    }
                    if(rowHold - 1 == 1 && pieceCheck(board, new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold), null))){
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold), PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold), PieceType.BISHOP));
                    }
                    if(rowHold - 1 > 0 && colHold + 1 < 9 && board.getPiece(new ChessPosition(rowHold - 1, colHold + 1)) != null && !Objects.equals(board.getPiece(new ChessPosition(rowHold - 1, colHold + 1)).getTeamColor().toString(), "BLACK")){
                        if(rowHold - 1 == 1){
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold + 1), PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold + 1), PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold + 1), PieceType.ROOK));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold + 1), PieceType.KNIGHT));
                        }
                        else moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold + 1), null));
                    }
                    if(rowHold - 1 > 0 && colHold - 1 > 0 && board.getPiece(new ChessPosition(rowHold - 1, colHold - 1)) != null && !Objects.equals(board.getPiece(new ChessPosition(rowHold - 1, colHold - 1)).getTeamColor().toString(), "BLACK")){
                        if(rowHold - 1 == 1){
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold - 1), PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold - 1), PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold - 1), PieceType.ROOK));
                            moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold - 1), PieceType.KNIGHT));
                        }
                        else moves.add(new ChessMove(myPosition, new ChessPosition(rowHold - 1, colHold - 1), null));
                    }
                }
                break;
        }
        return moves;
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
