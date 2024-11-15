package ui;

import java.util.Scanner;

public class Repl {
    private final ChessClient client;
    public Repl(String serverUrl){
        client = new ChessClient(serverUrl);
    }
    public void run() {
        System.out.println("Hello brave warrior. Sign in to start.");
        System.out.print(client.help());

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
    private void printPrompt(){
        System.out.print("\n>>>");
    }
}
