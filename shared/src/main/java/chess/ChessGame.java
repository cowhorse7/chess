package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessBoard cBoard;
    private TeamColor turn;

    public ChessGame() {
        cBoard=new ChessBoard();
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
        ChessBoard boardCopy = new ChessBoard(cBoard);
        ChessGame.TeamColor team = cBoard.getPiece(move.getStartPosition()).getTeamColor();
        movePiece(move); //should this be makeMove?
        if(isInCheck(team)){
            cBoard = boardCopy;
            return false;
        }
        cBoard = boardCopy;
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
        if(cBoard.getPiece(startPosition) == null){
            return null;
        }
        Collection<ChessMove> vMoves = cBoard.getPiece(startPosition).pieceMoves(cBoard, startPosition);
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
        ChessPiece mover = cBoard.getPiece(move.getStartPosition());
        cBoard.makeNullSpace(move.getStartPosition());
        if (cBoard.getPiece(move.getEndPosition()) != null){ cBoard.makeNullSpace(move.getEndPosition());}
        if (mover.getPieceType() == ChessPiece.PieceType.PAWN && (move.getEndPosition().getRow()==8 || move.getEndPosition().getRow()==1)){
            ChessPiece.PieceType upgrade = move.getPromotionPiece();
            mover = new ChessPiece(mover.getTeamColor(), upgrade);
        }
        cBoard.addPiece(move.getEndPosition(), mover);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(cBoard.getPiece(move.getStartPosition()) == null){ throw new InvalidMoveException(); }
        ChessGame.TeamColor team = cBoard.getPiece(move.getStartPosition()).getTeamColor();
        if(isInCheck(team) && !forwardMove(move)){throw new InvalidMoveException();}
        Collection<ChessMove> vMoves = validMoves(move.getStartPosition());
        if (vMoves.isEmpty() || !vMoves.contains(move)){throw new InvalidMoveException();}

        TeamColor gameTime = getTeamTurn();
        if (team != gameTime){ throw new InvalidMoveException(); }
        movePiece(move);

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
        //declare arrary of ChessPositions (which will hold the endPositions of the opposite team's valid chessmoves)
        //could also be of chessmoves
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                //this.spaces[i][j] = c.spaces[i][j];
            }
        }//should be able to iterate through every space on the board. If null or matching teamColor, continue. else, collect the pieceMoves and add them to [array]
        //when you find the king of teamColor, save the space it is on in a ChessPosition variable.
        //iterate through [array]. If the king's position is in the array of moves, return true.
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
        cBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return cBoard;
    }
}
