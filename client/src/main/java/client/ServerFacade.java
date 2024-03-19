package client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import client.*;

public class ServerFacade {

    private String urlString = "http://localhost:8080/";

    private ClientCommunicator setClientCommunication(String path) throws IOException {
        URL url = new URL(urlString + path);
        return new ClientCommunicator(url);
    }

    public static boolean returned_error = false;

    public void register(String input) throws IOException {
        ClientCommunicator communicator = setClientCommunication("/user");
        communicator.post(input, "register");
    }

    public void login(String input) throws IOException {
        ClientCommunicator communicator = setClientCommunication("/session");
        communicator.post(input, "login");
    }

    public void logout() throws IOException{
        ClientCommunicator communicator = setClientCommunication("/session");
        communicator.delete();
    }

    public void list(String input) throws IOException{
        ClientCommunicator communicator = setClientCommunication("/game");
        communicator.get();
    }

    public void join(String input) throws IOException{
        ClientCommunicator communicator = setClientCommunication("/game");
        communicator.put(input);
    }

    public void create(String input) throws IOException{
        ClientCommunicator communicator = setClientCommunication("/game");
        communicator.post(input, "create");
    }

    public void clear() throws IOException{
        ClientCommunicator communicator = setClientCommunication("/db");
        communicator.delete();
    }


}
