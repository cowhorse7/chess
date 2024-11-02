package service;

public class LoginResponse {
    private final String username;
    private final String authToken;
    public LoginResponse(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }
}
