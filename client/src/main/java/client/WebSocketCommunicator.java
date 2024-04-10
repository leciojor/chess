package client;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import server.websocket.Connection;
import ui.ReadEvaluateSourceInput;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.subMessages.ErrorMessage;
import webSocketMessages.serverMessages.subMessages.LoadGame;
import webSocketMessages.serverMessages.subMessages.Notification;
import webSocketMessages.userCommands.subCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebSocketCommunicator extends Endpoint {

    private Gson gson = new Gson();
    private URL url;
    private Connection connection;
    private Session session;

    //logic to receive msg from server
    private static void notify(String message) {
        Gson gson = new Gson();
        ServerMessage message_server = gson.fromJson(message, ServerMessage.class);

        switch (message_server.getServerMessageType()) {
            case NOTIFICATION -> {

                Notification final_message = gson.fromJson(message, Notification.class);
                displayNotification(final_message);
            }
            case ERROR -> {

                ErrorMessage final_message = gson.fromJson(message, ErrorMessage.class);
                displayError(final_message);}
            case LOAD_GAME -> {

                LoadGame final_message = gson.fromJson(message, LoadGame.class);
                loadGame(final_message);}
        }
    }
    //just print out messages
    private static void displayNotification(Notification message){
        ServerFacade.returned_error = false;
        System.out.println("NOTIFICATION: " + message.getMessage());
    }

    private static void displayError(ErrorMessage message){
        ServerFacade.returned_error = true;
        System.out.println("ERROR: " + message.getErrorMessage());
    }

    private static void loadGame(LoadGame message){
        ServerFacade.returned_error = false;
        System.out.println();
        System.out.println("CURRENT GAME:");
        System.out.println();
//        System.out.println("TEST LOAD " + message.getGame() == null);
        ReadEvaluateSourceInput.setCurrentBoard(message.getGame());
        ReadEvaluateSourceInput.printCurrentBoard(message.getGame(), ReadEvaluateSourceInput.getCurrentColor());
    }

    //needs to add logic so it can send messages to server any type and something to get the server messages and answer it (notify)

    public WebSocketCommunicator(String url) throws IOException, DeploymentException, URISyntaxException {
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);


        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                Gson gson = new Gson();
                try {
                    WebSocketCommunicator.notify(message);
                } catch(Exception ex) {
                    String error_json = gson.toJson(new ErrorMessage(ex.getMessage(), ServerMessage.ServerMessageType.ERROR));
                    WebSocketCommunicator.notify(error_json);
                }
            }        });

    }

    public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


    //send messages to server with current authtoken and the requested command (use SEND local method for that)

    public void sendJoin(int gameID, ChessGame.TeamColor playerColor, String authToken) throws Exception {
        JoinPlayer join_command = new JoinPlayer(gameID, playerColor, authToken);
        String join_json = gson.toJson(join_command);

        send(join_json);
    }

    public void sendObserve(int gameID, String authToken) throws Exception {
        JoinObserver observe_command = new JoinObserver(gameID, authToken);
        String observe_json = gson.toJson(observe_command);

        send(observe_json);
    }

    public void sendMove(int gameID, ChessMove move, String authToken) throws Exception {
        MakeMove move_command = new MakeMove(gameID, move, authToken);
        String move_json = gson.toJson(move_command);

        send(move_json);
    }

    public void sendLeave(int gameID, String authToken) throws Exception {
        Leave leave_command = new Leave(gameID, authToken);
        String leave_json = gson.toJson(leave_command);

        send(leave_json);
    }

    public void sendResign(int gameID, String authToken) throws Exception {
        Resign resign_command = new Resign(gameID, authToken);
        String resign_json = gson.toJson(resign_command);

        send(resign_json);

    }






}
