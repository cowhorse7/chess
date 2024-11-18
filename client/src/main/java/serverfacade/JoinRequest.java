package serverfacade;

import java.util.Objects;

public class JoinRequest {
    private final String playerColor;
    private final int gameID;

    public JoinRequest(String playerColor, int gameID) throws Exception {
        this.playerColor = playerColor.toUpperCase();
        checkColor(this.playerColor);
        this.gameID = gameID;
    }
    public int getGameID(){return gameID;}
    public String getPlayerColor(){return playerColor;}
    private void checkColor(String playerColor) throws Exception {
        if(!Objects.equals(playerColor, "WHITE") && !Objects.equals(playerColor, "BLACK") && !Objects.equals(playerColor, "OBSERVE")){
            throw new Exception("bad request");
        }
    }
}
