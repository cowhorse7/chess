package serverfacade;

import chess.ChessBoard;
import com.google.gson.Gson;

import model.AuthData;
import model.GameData;
import model.UserData;
import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;
    private String authToken = null;
    public ServerFacade(String url){
        serverUrl = url;
    }
    public AuthData createUser(UserData user) throws Exception {
        if (user.username() == null || user.password() == null || user.email() == null){throw new Exception("incomplete user data");}
        if (user.email().indexOf('@') < 0) {throw new Exception("email must contain @");}
        String path = "/user";
        AuthData auth = this.makeRequest("POST", path, user, AuthData.class);
        authToken = auth.authToken();
        return auth;
    }
    public AuthData loginUser(UserData user) throws Exception {
        String path = "/session";
        AuthData auth = this.makeRequest("POST", path, user, AuthData.class);
        authToken = auth.authToken();
        return auth;
    }
    public void logoutUser() throws Exception {
        String path = "/session";
        makeRequest("DELETE", path, null, null);
        authToken = null;
    }
    public int createChessGame(String gameName) throws Exception {
        if(gameName == null){throw new Exception("no name for game");}
        String path = "/game";
        CreateRequest newGame = new CreateRequest(gameName);
        GameData creation = this.makeRequest("POST", path, newGame, GameData.class);
        return creation.gameID();
    }
    public String listGames(int gameID) throws Exception {
        String path = "/game";
        ListGamesResponse list = this.makeRequest("GET", path, null, ListGamesResponse.class);
        if (gameID > 0){return new Gson().toJson(list.game(gameID), ChessBoard.class);}
        return list.toString();
    }
    public void clear() throws Exception {
        String path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }
    public void joinGame(int gameId, String playerColor) throws Exception {
        String path = "/game";
        JoinRequest request = new JoinRequest(playerColor, gameId);
        this.makeRequest("PUT", path, request, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null){
                http.addRequestProperty("authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws Exception {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            String errMsg = "";
            if (http.getContentLength() < 0) {
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader reader = new InputStreamReader(respBody);
                    Message message =  new Gson().fromJson(reader, Message.class);
                    errMsg = message.message();
                }
            }
            throw new Exception(STR."failure: \{status}" + errMsg);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws Exception {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
