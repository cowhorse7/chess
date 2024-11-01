package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import service.Service;
import service.joinRequest;
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

        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }
//FIXME: on errors, result is being set to an html error message instead of the expected
// datatype. Need to work on error handling.
    private String createUser(Request req, Response res) throws Exception{
        UserData newUser = serializer.fromJson(req.body(), UserData.class);
        AuthData result = service.registerUser(newUser);
        return serializer.toJson(result);
    }
    private String loginUser(Request req, Response res) throws Exception{
        UserData user = serializer.fromJson(req.body(), UserData.class);
        AuthData result = service.loginUser(user.username(),user.password());
        return serializer.toJson(result);
    }
    private String logoutUser(Request req, Response res) throws Exception{
        String authToken = req.headers("authorization");
        service.logoutUser(authToken);
        return serializer.toJson(null);
    }
    private String clearDatabase(Request req, Response res) throws Exception{
        service.clear();
        return serializer.toJson(null);
    }
    private String joinGame(Request req, Response res) throws Exception{
        String authToken = req.headers("authorization");
        joinRequest playerInfo = serializer.fromJson(req.body(), joinRequest.class);
        service.joinGame(authToken, playerInfo.getPlayerColor(), playerInfo.getGameID());
        return serializer.toJson(null);
    }
    //FIXME: "expected JSON, got 1" ↓
    private String createGame(Request req, Response res) throws Exception{
        String authToken = req.headers("authorization");
        String gameName = req.body();
        int result = service.createGame(authToken, gameName);
        return serializer.toJson(result);
    }
    private String listGames(Request req, Response res) throws Exception{
        String authToken = req.headers("authorization");
        ArrayList<GameData> result = service.listGames(authToken);
        return serializer.toJson(result);
    }

    private void exceptionHandler(Exception ex, Request req, Response res){
        res.status(500);
        res.body(serializer.toJson(Map.of("message", ex.getMessage())));
        ex.printStackTrace(System.out);
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
