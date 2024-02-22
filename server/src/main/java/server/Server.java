package server;

import dataAccess.DataAccessException;
import services.*;
import spark.*;
import com.google.gson.Gson;


public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        // Register your endpoints and handle exceptions here.
        //Spark.delete("/db", (request, response) -> clearGameHandler(request, response));
        Spark.post("/user", (request, response) -> registerHandler(request, response));
        Spark.post("/session", (request, response) -> loginHandler(request, response));
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
    private String registerHandler(Request req, Response res) {
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

    private String loginHandler(Request req, Response res) {
        res.header("Content-Type", "application/json");
        Gson gson = new Gson();
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

//    private String logOutHandler(Request req, Response res) throws DataAccessException {
        //res.header("Content-Type", "application/json");
//
//    }
}

