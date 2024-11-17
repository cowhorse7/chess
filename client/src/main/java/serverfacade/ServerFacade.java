package serverfacade;

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
    public void logoutUser(String authToken) throws Exception {
        String path = "/session";
        authToken = null;
        makeRequest("DELETE", path, authToken, null);
    }
    public int createChessGame(String authToken, String gameName) throws Exception {
        String path = "/game";
        return this.makeRequest("POST", path, gameName, Integer.class);
    }
    public ListGamesResponse listGames(String authToken) throws Exception {
        String path = "/game";
        return this.makeRequest("GET", path, authToken, ListGamesResponse.class);
    }
    public void clear() throws Exception {
        String path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }
    public void joinGame(String authToken, JoinRequest request) throws Exception {
        String path = "/game";
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
            throw new Exception(STR."failure: \{status}");
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
