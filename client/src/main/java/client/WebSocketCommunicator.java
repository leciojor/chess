package client;

import com.google.gson.Gson;
import server.websocket.Connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebSocketCommunicator {

    private Gson gson = new Gson();
    private URL url;
    private Connection connection;


    public WebSocketCommunicator(URL url, Connection connection) throws IOException {
        this.url = url;
        this.connection = connection;
    }

    public void join() throws IOException{

    }

    public void observe() throws IOException{

    }

    public void leave() throws IOException{

    }

    public void resign() throws IOException{

    }

    public void move() throws IOException{

    }





}
