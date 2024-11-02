package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import service.*;
import service.Service;
import spark.*;

import java.util.ArrayList;
import java.util.Map;

public class Server {
    private final UserDAO userDataAccess = new MemoryUserDAO();
    private final AuthDAO authDataAccess = new MemoryAuthDAO();
    private final GameDAO gameDataAccess = new MemoryGameDAO();
    private final Service service = new Service(userDataAccess, authDataAccess, gameDataAccess);
    private final Gson serializer = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //Spark.post("/user", this::exceptionHandler);
        Spark.post("/user", this::createUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.delete("/db", this::clearDatabase);
        Spark.put("/game", this::joinGame);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.exception(Exception.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String createUser(Request req, Response res) throws Exception {
        UserData newUser = serializer.fromJson(req.body(), UserData.class);
        AuthData result = service.registerUser(newUser);
        //if (result)
        return serializer.toJson(result);
    }

    private String loginUser(Request req, Response res) throws Exception {
        UserData user = serializer.fromJson(req.body(), UserData.class);
        LoginResponse result = service.loginUser(user.username(), user.password());
        return serializer.toJson(result);
    }

    private String logoutUser(Request req, Response res) throws Exception {
        String authToken = req.headers("authorization");
        service.logoutUser(authToken);
        return serializer.toJson(null);
    }

    private String clearDatabase(Request req, Response res) throws Exception {
        service.clear();
        return serializer.toJson(null);
    }

    private String joinGame(Request req, Response res) throws Exception {
        String authToken = req.headers("authorization");
        JoinRequest playerInfo = serializer.fromJson(req.body(), JoinRequest.class);
        service.joinGame(authToken, playerInfo.getPlayerColor(), playerInfo.getGameID());
        return serializer.toJson(null);
    }

    private String createGame(Request req, Response res) throws Exception {
        String authToken = req.headers("authorization");
        CreateRequest gameName = serializer.fromJson(req.body(), CreateRequest.class);
        int result = service.createGame(authToken, gameName.getGameName());
        GameData resultForJson = new GameData(result, null, null, null, null);
        return serializer.toJson(resultForJson);
    }

    private String listGames(Request req, Response res) throws Exception {
        String authToken = req.headers("authorization");
        ListGamesResponse result = service.listGames(authToken);
        return serializer.toJson(result);
    }

    private Object exceptionHandler(Exception ex, Request req, Response res) {
        if (ex.getMessage().equals("Error: unauthorized") || ex.getMessage().equals("Error: user does not exist")) {
            res.status(401);
        } else if (ex.getMessage().equals("Error: already taken")) {
            res.status(403);
        } else if (ex.getMessage().equals("Error: bad request")) {
            res.status(400);
        } else {
            res.status(500);
        }
        res.body(serializer.toJson(Map.of("message", ex.getMessage())));
        return res.body();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
