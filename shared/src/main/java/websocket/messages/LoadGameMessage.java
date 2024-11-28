package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    private ChessGame game; //FIXME: the specs say "any", and I've put "ChessGame"... not sure what that means?
    public LoadGameMessage(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }
    public ChessGame getGame(){return game;}
}
