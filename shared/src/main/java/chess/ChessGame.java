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

    public ChessBoard cBoard;
    private TeamColor turn;

    public ChessGame() {
        cBoard=new ChessBoard();
        cBoard.resetBoard();
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
        ArrayList<ChessPosition>enemyEndSquares = new ArrayList<>();
        ChessPosition kingSpot = null;
        //iterate through every space on the board. If enemy, collect the pieceMoves
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition currentSquare = new ChessPosition(i, j);
                //checks if a given space contains a teammate or nothing so that we can handle enemy pieces in else{}
                if (cBoard.getPiece(currentSquare) == null) {continue;}
                else if (cBoard.getPiece(currentSquare).getTeamColor() == teamColor){
                    if(cBoard.getPiece(currentSquare).getPieceType()== ChessPiece.PieceType.KING){
                        kingSpot = currentSquare;
                    }
                    continue;
                }
                else{
                    Collection<ChessMove>enemyMoves = cBoard.getPiece(currentSquare).pieceMoves(cBoard, currentSquare);
                    for(ChessMove e : enemyMoves){
                        enemyEndSquares.add(e.getEndPosition());
                    }
                }
            }
        }
        if (kingSpot == null){
            return false;
        }
        for(ChessPosition p : enemyEndSquares){
            if (p.equals(kingSpot)){
                return true;
            }
        }
        return false;
    }

    public boolean noRemainingMoves(TeamColor teamColor){
        ArrayList<ChessMove>vMoves = new ArrayList<>();
        for(int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currentSquare = new ChessPosition(i, j);
                if (cBoard.getPiece(currentSquare) == null || cBoard.getPiece(currentSquare).getTeamColor() != teamColor) {
                    continue;
                } else {
                    Collection<ChessMove>possibleMoves = validMoves(currentSquare);
                    vMoves.addAll(possibleMoves);
                }
            }
        }
        return vMoves.isEmpty();
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noRemainingMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && noRemainingMoves(teamColor);
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
