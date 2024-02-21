package server;

import services.RegisterService;
import spark.*;
import com.google.gson.Gson;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("C:\\Users\\lecio\\CS240\\chess\\server\\src\\main\\resources\\web");

        // Register your endpoints and handle exceptions here.
        //Spark.delete("/db", (request, response) -> clearGameHandler(request, response));
        Spark.post("/user", (request, response) -> registerHandler(request, response));
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

    //private Object clearGameHandler(Request req, Response res){
       //return clearGameService(Request req, Response, res);
    //}

    //USE INHERITANCE FOR THE HANDLERS FOR AVOIDING DUPLICATE CODE or just put the body code in the lambdas
    private String registerHandler(Request req, Response res){
        Gson gson = new Gson();
        RegisterRequest register = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterService service = new RegisterService(register);
        RegisterResponse response = service.register(register.getUsername(), register.getPassword(), register.getEmail());
        return gson.toJson(response);
    }
}
