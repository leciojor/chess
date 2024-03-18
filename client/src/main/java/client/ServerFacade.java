package client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import client.*;

public class ServerFacade {

    public ServerFacade() throws IOException {}

    public void register(String input){
        ClientCommunicator.post("/user", input);
    }

    public void login(String input){
        ClientCommunicator.post("/session", input);
    }

    public void logout(String input){
        ClientCommunicator.delete("/session", input);
    }

    public void list(String input){
        ClientCommunicator.get("/game", input);
    }

    public void join(String input){
        ClientCommunicator.put("/game", input);
    }

    public void create(String input){
        ClientCommunicator.post("/game", input);
    }

    public void clear(String input){
        ClientCommunicator.delete("/db", input);
    }


}
