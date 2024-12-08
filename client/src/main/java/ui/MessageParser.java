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
                //LoadGameMessage game = (LoadGameMessage) serverMessage;
                message = client.redraw();
                break;
        }
        return message;
    }
//    public String getMessage(ServerMessage serverMessage) throws Exception {
//        String message = "";
//        if (serverMessage instanceof ErrorMessage errorMessage) {
//            message = errorMessage.getErrorMessage();
//        }
//        else if(serverMessage instanceof NotificationMessage note) {
//            message = note.getMessage();
//        }
//        else if(serverMessage instanceof LoadGameMessage) {
//            message = client.redraw();
//        }
//        return message;
//    }
}
