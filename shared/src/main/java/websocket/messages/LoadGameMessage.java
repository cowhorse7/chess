package websocket.messages;

public class LoadGameMessage extends ServerMessage{
    private Integer gameID;
    public LoadGameMessage(ServerMessageType type, Integer gameID) {
        super(type);
        this.gameID = gameID;
    }
    public Integer getGame(){return gameID;}
}
