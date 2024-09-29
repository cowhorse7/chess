package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessBoard board;
    public TeamColor turn;
    public ChessGame() {
        board=new ChessBoard();
        setBoard(board);
        turn = TeamColor.WHITE;
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(board.getPiece(startPosition) == null){
            return null;
        }
        Collection<ChessMove> vMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
//        ArrayList<int>rem = new ArrayList<int>();
//        for (int i = 0; i < vMoves.size(); i++){
//            if(vMoves[i]){
//                //if move causes check, add index val to rem
//            }
//        }
        //iterate through rem and remove those index vals from vMoves
        return vMoves;
    }

    public void movePiece(ChessMove move){
        ChessPiece mover = board.getPiece(move.getStartPosition());
        board.makeNullSpace(move.getStartPosition());
        if (board.getPiece(move.getEndPosition()) != null){ board.makeNullSpace(move.getEndPosition());}
        board.addPiece(move.getEndPosition(), mover);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        TeamColor gameTime = getTeamTurn();
        if (board.getPiece(move.getStartPosition()).getTeamColor() != gameTime){ throw new InvalidMoveException();}
        //if move in validmoves(move.startposition): movePiece
        //note that a chessMove has start,end, and promPiece
        //if promPiece!=null, change pieceType
        if (gameTime == TeamColor.WHITE){setTeamTurn(TeamColor.BLACK);}
        else {setTeamTurn(TeamColor.WHITE);}
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        board.resetBoard();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

}
