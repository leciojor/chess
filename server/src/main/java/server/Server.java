package server;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import server.requests.*;
import server.responses.*;
import services.*;
import spark.*;
import com.google.gson.Gson;
import server.websocket.WebSocketHandler;


import java.sql.SQLException;


public class Server {

    private final WebSocketHandler webSocketHandler = new WebSocketHandler();


    public int run(int desiredPort){
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            createTables();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }


        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/connect", webSocketHandler);
        Spark.delete("/db", (request, response) -> clearGameHandler(request, response));
        Spark.post("/user", (request, response) -> registerHandler(request, response));
        Spark.post("/session", (request, response) -> loginHandler(request, response));
        Spark.delete("/session", (request, response) -> logoutHandler(request, response));
        Spark.get("/game", (request, response) -> listGamesHandler(request, response));
        Spark.post("/game", (request, response) -> createGameHandler(request, response));
        Spark.put("/game", (request, response) -> joinGameHandler(request, response));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void createTables() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            var createUserTable = """
            CREATE TABLE  IF NOT EXISTS User (
                id INT NOT NULL AUTO_INCREMENT,
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                authtoken VARCHAR(255),
                FOREIGN KEY (authtoken) REFERENCES Auth(authtoken),
                PRIMARY KEY (id)
            )""";

            var createAuthTable = """
            CREATE TABLE  IF NOT EXISTS Auth (
                authtoken VARCHAR(255) NOT NULL,
                created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP(3),
                username VARCHAR(255) NOT NULL,
                PRIMARY KEY (authtoken)
            )""";

            var createGameTable = """
            CREATE TABLE  IF NOT EXISTS Game (
                gameid INT NOT NULL,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255),
                game TEXT NOT NULL,
                PRIMARY KEY (gameid)
            )""";

            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }

            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                createTableStatement.executeUpdate();
            }

            try (var createTableStatement = conn.prepareStatement(createGameTable)) {
                createTableStatement.executeUpdate();
            }
        }


    }



    private String registerHandler(Request req, Response res) throws SQLException, DataAccessException {
        res.header("Content-Type", "application/json");
        Gson gson = new Gson();
        RegisterRequest register = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterService service = new RegisterService(register);
        RegisterResponse response = service.register(register.getUsername(), register.getPassword(), register.getEmail());
        if (response.getStatus() == 200){
            res.status(200);
            return gson.toJson(response);
        }
        else{
            res.status(response.getStatus());
            return gson.toJson(response);
        }
    }

    private String loginHandler(Request req, Response res) throws SQLException, DataAccessException{
        res.header("Content-Type", "application/json");
        Gson gson = new Gson();
        //System.out.print(req.body());
        LoginRequest login = gson.fromJson(req.body(), LoginRequest.class);
        LoginService service = new LoginService(login);
        LoginResponse response = service.login(login.getUsername(), login.getPassword());
        if (response.getStatus() == 200){
            res.status(200);
            return gson.toJson(response);
        }
        else{
            res.status(response.getStatus());
            return gson.toJson(response);
        }
    }



    private String logoutHandler(Request req, Response res) throws SQLException, DataAccessException{
        res.header("Content-Type", "application/json");
        Gson gson = new Gson();
        String token = "{" + "\"Authorization\":" + req.headers("Authorization") + "}";
        LogoutRequest logout = gson.fromJson(token, LogoutRequest.class);
        LogoutService service = new LogoutService(logout);
        LogoutResponse response = service.logout(req.headers("Authorization"));
        if (response.getStatus() == 200){
            res.status(200);
            return gson.toJson(response);
        }
        else{
            res.status(response.getStatus());
            return gson.toJson(response);
        }
    }

    private String listGamesHandler(Request req, Response res) throws SQLException, DataAccessException{
        res.header("Content-Type", "application/json");
        Gson gson = new Gson();
        String token = "{" + "\"Authorization\":" + req.headers("Authorization") + "}";
        ListGamesRequest listGames = gson.fromJson(token, ListGamesRequest.class);
        ListGamesService service = new ListGamesService(listGames);
        ListGamesResponse response = service.listGames(req.headers("Authorization"));
        if (response.getStatus() == 200){
            res.status(200);
            return gson.toJson(response);
        }
        else{
            res.status(response.getStatus());
            return gson.toJson(response);
        }
    }

    private String createGameHandler(Request req, Response res) throws SQLException, DataAccessException{
        res.header("Content-Type", "application/json");
        Gson gson = new Gson();
        CreateGameRequest createGame = gson.fromJson(req.body(), CreateGameRequest.class);
        CreateGameService service = new CreateGameService(createGame);
        CreateGameResponse response = service.createGame(createGame.getName(), req.headers("Authorization"));
        if (response.getStatus() == 200){
            res.status(200);
            return gson.toJson(response);
        }
        else{
            res.status(response.getStatus());
            return gson.toJson(response);
        }

    }

    private String joinGameHandler(Request req, Response res) throws SQLException, DataAccessException{
        res.header("Content-Type", "application/json");
        Gson gson = new Gson();
        JoinGameRequest joinGame = gson.fromJson(req.body(), JoinGameRequest.class);
        JoinGameService service = new JoinGameService(joinGame);
        JoinGameResponse response = service.joinGame(joinGame.getColor(), joinGame.getid(), req.headers("Authorization"));
        if (response.getStatus() == 200){
            res.status(200);
            return gson.toJson(response);
        }
        else{
            res.status(response.getStatus());
            return gson.toJson(response);
        }

    }

    private String clearGameHandler(Request req, Response res) throws SQLException, DataAccessException{
        res.header("Content-Type", "application/json");
        Gson gson = new Gson();
        ClearService service = new ClearService();
        ClearResponse response = service.clear();
        if (response.getStatus() == 200){
            res.status(200);
            return gson.toJson(response);
        }
        else{
            res.status(response.getStatus());
            return gson.toJson(response);
        }

    }
}

