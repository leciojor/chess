package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("C:\\Users\\lecio\\CS240\\chess\\server\\src\\main\\resources\\web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (request, response) -> clearGameHandler(request, response));
        //Spark.post("/user", (request, response) -> registerHandler(request, response));
        //Spark.post("/session", (request, response) -> loginHandler(request, response));
        //Spark.delete("/session", (request, response) -> logOutHandler(request, response));
        //Spark.get("/game", (request, response) -> listGamesHandler(request, response));
        //Spark.post("/game", (request, response) -> createGameHandler(request, response));
        //Spark.put("/game", (request, response) -> joinGameHandler(request, response));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}