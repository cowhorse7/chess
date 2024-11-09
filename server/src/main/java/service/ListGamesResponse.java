package service;

import model.GameData;
import java.util.HashSet;

public class ListGamesResponse {
    private final HashSet<GameData> games;

    public ListGamesResponse(HashSet<GameData> listOfGames){
        games = listOfGames;
    }
    public int listSize(){return games.size();}
}
