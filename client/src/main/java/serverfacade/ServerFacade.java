package serverfacade;

import chess.ChessBoard;
import com.google.gson.Gson;

import model.AuthData;
import model.GameData;
import model.UserData;
import websocket.commands.UserGameCommand;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;
    private String authToken = null;
    public ServerFacade(String url){
        serverUrl = url;
    }
    public ListGamesResponse listOfGames = null;
    public AuthData createUser(UserData user) throws Exception {
        if (user.username() == null || user.password() == null || user.email() == null){
            throw new Exception("incomplete user data");
        }
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
    public String listGames() throws Exception {
        String path = "/game";
        listOfGames = this.makeRequest("GET", path, null, ListGamesResponse.class);
        return listOfGames.toString();
    }
    public void clear() throws Exception {
        String path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }
    public String getGame(int gameNum) throws Exception{
        if (listOfGames == null){throw new Exception("You must \"list\" before joining");}
        if (!listOfGames.linkedGames.containsKey(gameNum)){throw new Exception("Game does not exist");}
        int gameID = listOfGames.linkedGames.get(gameNum);
        return new Gson().toJson(listOfGames.game(gameID), ChessBoard.class);
    }
    public void joinGame(int gameNum, String playerColor) throws Exception {
        if (listOfGames == null){throw new Exception("You must \"list\" before joining");}
        if (listOfGames.linkedGames == null){throw new Exception("No games exist");}
        if (!listOfGames.linkedGames.containsKey(gameNum)){throw new Exception("Game does not exist");}
        String path = "/game";
        int gameID = listOfGames.linkedGames.get(gameNum);
        JoinRequest request = new JoinRequest(playerColor, gameID);
        this.makeRequest("PUT", path, request, null);
    }
//    public void leaveGame(int gameNum) throws Exception {
//        int gameID = listOfGames.linkedGames.get(gameNum);
//        String path = "/ws";
//        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
//        this.makeRequest("websocket", path, command, null);
//    }

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
                try (InputStream respBody = http.getErrorStream()) {
                    InputStreamReader reader = new InputStreamReader(respBody);
                    Message message =  new Gson().fromJson(reader, Message.class);
                    errMsg = message.message();
                }
            }
            throw new Exception(errMsg);
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
