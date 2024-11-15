package serverfacade;

import com.google.gson.Gson;

import model.UserData;
import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String url){
        serverUrl = url;
    }
    public UserData createUser(UserData user) throws Exception {
        String path = "/user";
        return this.makeRequest("POST", path, user, UserData.class);
    }
    public Object loginUser(){
        String path = "/session";
        return null;
    }
    public void logoutUser(){String path = "/session";}
    public Object createChessGame(){
        String path = "/game";
        return null;
    }
    public Object listGames(){
        String path = "/game";
        return null;
    }
    public void clear(){
        String path = "/db";
    }
    public void joinGame(){
        String path = "/game";
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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
