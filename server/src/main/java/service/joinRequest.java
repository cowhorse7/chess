package service;

import java.util.Objects;

public class joinRequest {
    private String playerColor;
    private int gameID;

    public joinRequest(String playerColor, int gameID) throws ServiceException {
        this.playerColor = playerColor;
        checkColor(playerColor);
        this.gameID = gameID;
    }
    public int getGameID(){return gameID;}
    public String getPlayerColor(){return playerColor;}
    private void checkColor(String playerColor) throws ServiceException {
        if(!Objects.equals(playerColor, "WHITE") && !Objects.equals(playerColor, "BLACK")){
            throw new ServiceException("bad request");
        }
    }
}
