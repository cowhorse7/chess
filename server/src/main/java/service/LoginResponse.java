package service;

public class LoginResponse {
    private String username;
    private String authToken;
    public LoginResponse(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }
}
