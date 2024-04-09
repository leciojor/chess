package client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import chess.ChessGame;
import chess.ChessMove;

import javax.websocket.DeploymentException;

public class ServerFacade {

    private int port;

    private String urlString = "http://localhost:";

    private String webSocString = "ws://localhost:";

    private ClientCommunicator setClientCommunication(String path) throws IOException {
        URL url = new URL(urlString + this.port + "/" +  path);
        return new ClientCommunicator(url);
    }

    private void determineWebSocketMethod(String webSocketMethod, WebSocketCommunicator communicator, Object[] requiredParameters) throws Exception {
        switch (webSocketMethod) {
            case "join_player" -> communicator.sendJoin((Integer) requiredParameters[0], (ChessGame.TeamColor) requiredParameters[1], (String) requiredParameters[2]);
            case "join_observer" -> communicator.sendObserve((Integer) requiredParameters[0], (String) requiredParameters[1]);
            case "make_move" -> communicator.sendMove((Integer) requiredParameters[0], (ChessMove) requiredParameters[1], (String) requiredParameters[2]);
            case "leave" -> communicator.sendLeave((Integer) requiredParameters[0], (String) requiredParameters[1]);
            case "resign" -> communicator.sendResign((Integer) requiredParameters[0], (String) requiredParameters[1]);
        }

    }

    private WebSocketCommunicator setWebSocketCommunication(String path) throws IOException, DeploymentException, URISyntaxException {
        String url = webSocString + this.port + "/" +  path;
        return new WebSocketCommunicator(url);
    }

    public ServerFacade(int port){
        this.port = port;
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

    public void list() throws IOException{
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

    public void webSoc(String webSocketMethod, Object[] requiredParameters) throws Exception {
        WebSocketCommunicator communicator = setWebSocketCommunication("connect");
        determineWebSocketMethod(webSocketMethod, communicator, requiredParameters);
    }







}
