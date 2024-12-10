package ui;

import websocket.messages.*;

public class MessageParser {
    private final ChessClient client;
    public MessageParser(ChessClient client){
        this.client = client;
    }
    public String getMessage(ServerMessage serverMessage) throws Exception {
        String message = "";
        switch (serverMessage.getServerMessageType()){
            case ERROR:
                ErrorMessage err = (ErrorMessage) serverMessage;
                message = err.getErrorMessage();
                break;
            case NOTIFICATION:
                NotificationMessage note = (NotificationMessage) serverMessage;
                message = note.getMessage();
                break;
            case LOAD_GAME:
                LoadGameMessage gameMessage = (LoadGameMessage) serverMessage;
                client.setBoard(gameMessage.getGame());
                message = client.redraw();
                break;
        }
        return message;
    }
}
