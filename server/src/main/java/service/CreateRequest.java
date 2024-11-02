package service;

public class CreateRequest {
    private final String gameName;
    public CreateRequest(String gameName){
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }
}
