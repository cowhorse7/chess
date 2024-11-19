package serverfacade;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

import java.util.HashSet;

public class ListGamesResponse {
    private final HashSet<GameData> games;

    public ListGamesResponse(HashSet<GameData> listOfGames){
        games = listOfGames;
    }
    public int listSize(){return games.size();}
    public ChessBoard game(int gameID){
        for(GameData gameIterator : games){
            if(gameIterator.gameID() != gameID){continue;}
            else{return gameIterator.game().cBoard;}
        }
        return null;
    }

    @Override
    public String toString() {
        if (listSize() == 0){
            return "No games to display\n";
        }
        StringBuilder response = new StringBuilder();
        for(GameData game : games){
            response.append("GAME ID: ").append(game.gameID()).
                    append(" NAME: ").append(game.gameName()).
                    append(" PLAYER WHITE: ").append(game.whiteUsername()).
                    append(" PLAYER BLACK: ").append(game.blackUsername()).
                    append("\n");
        }
        return response.toString();
    }
}
