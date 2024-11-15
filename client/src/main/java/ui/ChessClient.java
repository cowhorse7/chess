package ui;
import serverfacade.ServerFacade;
public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }
}
