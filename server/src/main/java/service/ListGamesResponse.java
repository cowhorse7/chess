package service;

import model.GameData;

import java.util.ArrayList;

public class ListGamesResponse {
    private ArrayList<GameData> games;

    public ListGamesResponse(ArrayList<GameData> listOfGames){
        games = listOfGames;
    }
}
