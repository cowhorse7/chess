package ui;

import serverfacade.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final ChessClient client;
    public Repl(String serverUrl){
        client = new ChessClient(serverUrl, this);
    }
    public void run() {
        System.out.println(SET_TEXT_ITALIC + SET_TEXT_COLOR_MAGENTA + "Hello brave warrior. Sign in to start.");
        System.out.print(SET_TEXT_COLOR_GREEN + client.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while(!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try{
                result = client.eval(line);
                System.out.print(result);
            }catch(Throwable e){
                String msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
    public void notify(ServerMessage serverMessage){
        System.out.println(SET_TEXT_COLOR_YELLOW + serverMessage);
        printPrompt();
    }
    private void printPrompt(){
        System.out.print(RESET_BG_COLOR + SET_TEXT_COLOR_GREEN + "\n>");
    }
}
