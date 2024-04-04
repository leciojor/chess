package client;

import com.google.gson.Gson;
import server.websocket.Connection;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.subMessages.ErrorMessage;
import webSocketMessages.serverMessages.subMessages.LoadGame;
import webSocketMessages.serverMessages.subMessages.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebSocketCommunicator {

    private Gson gson = new Gson();
    private URL url;
    private Connection connection;
    private Session session;

    //logic to receive msg from server
    private static void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            //cant cast here (deserialize again)
            case NOTIFICATION -> displayNotification(((Notification) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGame) message).getGameID());
        }
    }
    //just print out messages
    private static void displayNotification(Object message){

    }

    private static void displayError(Object message){

    }

    private static void loadGame(Object message){

    }

    //needs to add logic so it can send messages to server any type and something to get the server messages and answer it (notify)

    public WebSocketCommunicator(String url) throws IOException, DeploymentException, URISyntaxException {
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);


        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                try {
                    ServerMessage message_server = gson.fromJson(message, ServerMessage.class);
                    WebSocketCommunicator.notify(message_server);
                } catch(Exception ex) {
                    WebSocketCommunicator.notify(new ErrorMessage(ex.getMessage(), ServerMessage.ServerMessageType.ERROR));
                }
            }        });

    }

    public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}

    public void onOpen(Session session, EndpointConfig endpointConfig) {}


    //send messages to server with current authtoken and the requested command (use SEND local method for that)

    public void sendJoin() throws IOException {

    }

    public void sendObserve(){

    }

    public void sendMove(){

    }

    public void sendLeave(){

    }

    public void sendResign(){

    }






}
