package serverfacade;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.HashSet;

public class ListGamesResponse {
    private final HashSet<GameData> games;
    public HashMap<Integer, Integer> linkedGames = new HashMap<>();
    public ListGamesResponse(HashSet<GameData> listOfGames){
        games = listOfGames;
    }
    public int listSize(){return games.size();}
    public ChessGame game(int gameID){
        for(GameData gameIterator : games){
            if(gameIterator.gameID() != gameID){continue;}
            else{return gameIterator.game();}
        }
        return null;
    }
    public void updateGame(int gameID, ChessGame changedGame){
        for(GameData gameIterator : games){
            if(gameIterator.gameID() != gameID){continue;}
            else{
                games.remove(gameIterator);
                games.add(new GameData(gameID, gameIterator.whiteUsername(),
                    gameIterator.blackUsername(), gameIterator.gameName(), changedGame));
                break;
            }
        }
    }

    @Override
    public String toString() {
        if (listSize() == 0){
            return "No games to display\n";
        }
        StringBuilder response = new StringBuilder();
        int iterator = 0;
        linkedGames = new HashMap<>();
        for(GameData game : games){
            ++iterator;
            String white = game.whiteUsername();
            String black = game.blackUsername();
            if(white == null){white = "<none>";}
            if(black == null){black = "<none>";}
            linkedGames.put(iterator, game.gameID());
            response.append(iterator).append(": ").
                    append(game.gameName()).
                    append(" PLAYER WHITE: ").append(white).
                    append(" PLAYER BLACK: ").append(black).
                    append("\n");
        }
        return response.toString();
    }
}
