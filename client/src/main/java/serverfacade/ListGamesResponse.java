package serverfacade;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.HashSet;

public class ListGamesResponse {
    private final HashSet<GameData> games;
    public HashMap<Integer, Integer> linkedGames;
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
        int iterator = 0;
        linkedGames.clear();
        for(GameData game : games){
            ++iterator;
            linkedGames.put(iterator, game.gameID());
            response.append(iterator).append(": ").
                    append(game.gameName()).
                    append(" PLAYER WHITE: ").append(game.whiteUsername()).
                    append(" PLAYER BLACK: ").append(game.blackUsername()).
                    append("\n");
        }
        return response.toString();
    }
}
