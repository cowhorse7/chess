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
    private TeamColor turn;

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

    private boolean forwardMove(ChessMove move){
        ChessBoard boardCopy = new ChessBoard(board);
        ChessGame.TeamColor team = board.getPiece(move.getStartPosition()).getTeamColor();
        movePiece(move); //should this be makeMove?
        if(isInCheck(team)){
            board = boardCopy;
            return false;
        }
        board = boardCopy;
        return true;
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
        ArrayList<ChessMove>rem = new ArrayList<ChessMove>();
        for (ChessMove e : vMoves){
                if(!forwardMove(e)){
                    rem.add(e);
                }
        }
        for (ChessMove e : rem) {
            vMoves.remove(e);
        }
        return vMoves;
    }

    public void movePiece(ChessMove move){
        ChessPiece mover = board.getPiece(move.getStartPosition());
        board.makeNullSpace(move.getStartPosition());
        if (board.getPiece(move.getEndPosition()) != null){ board.makeNullSpace(move.getEndPosition());}
        if (mover.getPieceType() == ChessPiece.PieceType.PAWN && (move.getEndPosition().getRow()==8 || move.getEndPosition().getRow()==1)){
            ChessPiece.PieceType upgrade = move.getPromotionPiece();
            mover = new ChessPiece(mover.getTeamColor(), upgrade);
        }
        board.addPiece(move.getEndPosition(), mover);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()) == null){ throw new InvalidMoveException(); }
        ChessGame.TeamColor team = board.getPiece(move.getStartPosition()).getTeamColor();
        if(isInCheck(team) && !forwardMove(move)){throw new InvalidMoveException();}
        Collection<ChessMove> vMoves = validMoves(move.getStartPosition());
        if (vMoves.isEmpty() || !vMoves.contains(move)){throw new InvalidMoveException();}

        TeamColor gameTime = getTeamTurn();
        if (board.getPiece(move.getStartPosition()).getTeamColor() != gameTime){ throw new InvalidMoveException(); }

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
        if (teamColor == TeamColor.WHITE){
            //check if any black pieces can reach white king
        }
        if (teamColor == TeamColor.BLACK){

        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //if(isInCheck(teamColor) && validMoves is empty [for all pieces]){return true;}
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
        //if (!isInCheck(teamColor) && validMoves are empty for all remaining pieces){ return true;}
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
