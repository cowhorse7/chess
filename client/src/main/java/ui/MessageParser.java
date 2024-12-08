package ui;

import websocket.messages.*;

public class MessageParser {
    private final ChessClient client;
    public MessageParser(ChessClient client){
        this.client = client;
    }
    public String getMessage(ServerMessage serverMessage) throws Exception {
        String message = "";
        if (serverMessage instanceof ErrorMessage errorMessage) {
            message = errorMessage.getErrorMessage();
        }
        else if(serverMessage instanceof NotificationMessage note) {
            message = note.getMessage();
        }
        else if(serverMessage instanceof LoadGameMessage game) {
            message = client.redraw();
        }
        return message;
    }
}
