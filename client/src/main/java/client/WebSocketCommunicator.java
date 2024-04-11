package client;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
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

    private Session session;

    //logic to receive msg from server
    private static void notify(String message) {
        Gson gson = new Gson();
        ServerMessage messageServer = gson.fromJson(message, ServerMessage.class);

        switch (messageServer.getServerMessageType()) {
            case NOTIFICATION -> {

                Notification finalMessage = gson.fromJson(message, Notification.class);
                displayNotification(finalMessage);
            }
            case ERROR -> {

                ErrorMessage finalMessage = gson.fromJson(message, ErrorMessage.class);
                displayError(finalMessage);}
            case LOAD_GAME -> {

                LoadGame finalMessage = gson.fromJson(message, LoadGame.class);
                loadGame(finalMessage);}
        }
    }
    //just print out messages
    private static void displayNotification(Notification message){
        System.out.println("NOTIFICATION: " + message.getMessage());
    }

    private static void displayError(ErrorMessage message){
        System.out.println("ERROR: " + message.getErrorMessage());
    }

    private static void loadGame(LoadGame message){
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
            @Override
            public void onMessage(String message) {
                Gson gson = new Gson();
                try {
                    WebSocketCommunicator.notify(message);
                } catch(Exception ex) {
                    String errorJson = gson.toJson(new ErrorMessage(ex.getMessage(), ServerMessage.ServerMessageType.ERROR));
                    WebSocketCommunicator.notify(errorJson);
                }
            }        });

    }

    public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


    //send messages to server with current authtoken and the requested command (use SEND local method for that)

    public void sendJoin(int gameID, ChessGame.TeamColor playerColor, String authToken) throws Exception {
        JoinPlayer joinCommand = new JoinPlayer(gameID, playerColor, authToken);
        String joinJson = gson.toJson(joinCommand);

        send(joinJson);
    }

    public void sendObserve(int gameID, String authToken) throws Exception {
        JoinObserver observeCommand = new JoinObserver(gameID, authToken);
        String observeJson = gson.toJson(observeCommand);

        send(observeJson);
    }

    public void sendMove(int gameID, ChessMove move, String authToken) throws Exception {
        MakeMove moveCommand = new MakeMove(gameID, move, authToken);
        String moveJson = gson.toJson(moveCommand);

        send(moveJson);
    }

    public void sendLeave(int gameID, String authToken) throws Exception {
        Leave leaveCommand = new Leave(gameID, authToken);
        String leaveJson = gson.toJson(leaveCommand);

        send(leaveJson);
    }

    public void sendResign(int gameID, String authToken) throws Exception {
        Resign resignCommand = new Resign(gameID, authToken);
        String resignJson = gson.toJson(resignCommand);

        send(resignJson);

    }






}
