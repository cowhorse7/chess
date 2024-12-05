package ui;

import com.google.gson.Gson;
import serverfacade.ServerFacade;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class GamePlay {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;
    private final Gson serializer = new Gson();

    public GamePlay(String serverUrl) {
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> makeMove();
                case "resign" -> resign();
                case "highlight" -> highlight();

                default -> help();
            };
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + e.getMessage();
        }
    }
    public String help() {
//        if (state == State.SIGNEDOUT) {
//            return """
//                    - help
//                    - register <username> <password> <email>
//                    - login <username> <password>
//                    - quit
//                    """;
//        }else {
            return """
                    - help
                    - redraw -> show the current board
                    - leave -> exit game
                    - move -> move a piece on the board
                    - resign -> forfeit or end game
                    - highlight -> show legal moves for a piece
                    """;
        }
        public String redraw(){
        return null;
        }
    public String leave(){
        return null;
    }
    public String makeMove(){
        return null;
    }
    public String resign(){
        return null;
    }
    public String highlight(){
        return null;
    }
    }
