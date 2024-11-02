package service;

import model.GameData;

import java.util.Collection;
import java.util.HashSet;

public class ListGamesResponse {
    private HashSet<GameData> games;

    public ListGamesResponse(HashSet<GameData> listOfGames){
        games = listOfGames;
    }
}
