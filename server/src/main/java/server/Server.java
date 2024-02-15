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
    private registerResponse registerHandler(Request req, Response res){
        //follow the diagram
        //create Request and response classes to send the desialized objects
        //create service classes, instanciate them here and use methods with the desiarlized objects
        Gson gson = new Gson();
        registerRequest register = gson.fromJson(req.body(), registerRequest.class);
        RegisterService service = new RegisterService(register);
        service.register(register.getUsername(), register.getPassword(), register.getEmail());

        //register should return authtoken and username
        //serialize the response and then
        //STORE EVERYTHING IN MEMORY (DATAACCESS CLASSES)

        //return //body of serialization BACK (response);
    }
}
