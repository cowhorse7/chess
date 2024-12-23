import chess.*;
import ui.Repl;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("♕ 240 Chess Client: ");
        String serverUrl = "http://localhost:8080";
        if(args.length == 1){
            serverUrl = args[0];
        }
        new Repl(serverUrl).run();
    }
}