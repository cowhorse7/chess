package service;

public class CreateRequest {
    private String gameName;
    public CreateRequest(String gameName){
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }
}
