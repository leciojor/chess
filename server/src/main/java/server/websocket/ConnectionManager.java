package server.websocket;
import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.subMessages.LoadGame;
import webSocketMessages.serverMessages.subMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectionManager {

//gameId by a list with the conncetions
    private static final HashMap<Integer, List<Session>> connections = new HashMap<>();

    //sessionLoop may not be excluding the current user (REVIEW LOGIC)
    private void sessionLoop(int gameID, String json, Session userSession) throws IOException {
        List<Session> gameSessions = connections.get(gameID);

        for (Session session : gameSessions){

            if (session != userSession){
                session.getRemote().sendString(json);
            }

        }
    }

    private void sessionLoopAll(int gameID, String json) throws IOException {
        List<Session> gameSessions = connections.get(gameID);

        for (Session session : gameSessions){
            session.getRemote().sendString(json);
        }
    }

    public void add(int gameID, Session session){

        if (connections.containsKey(gameID)){
            List<Session> gameConnections = connections.get(gameID);
            gameConnections.add(session);
        }
        else{
            List<Session> gameConnections = new ArrayList<>();
            gameConnections.add(session);
            connections.put(gameID, gameConnections);
        }

    }

    public void remove(int gameID, Session session){
        if (connections.containsKey(gameID)){
            List<Session> gameConnections = connections.get(gameID);
            gameConnections.remove(session);
        }

    }

    public void sendNotifications(int gameID, String msg, Session userSession, boolean all) throws IOException {
        Gson gson = new Gson();
        //sessionLoop may not be excluding the current user (REVIEW LOGIC)
        Notification notification = new Notification(msg, ServerMessage.ServerMessageType.NOTIFICATION);
        String notificationJson = gson.toJson(notification);
        if (all){
            sessionLoopAll(gameID, notificationJson);
        }
        else{
            sessionLoop(gameID, notificationJson, userSession);
        }



    }

    public void sendLoad(ChessGame game, int gameID) throws IOException{
        Gson gson = new Gson();

        LoadGame load = new LoadGame(game, ServerMessage.ServerMessageType.LOAD_GAME);
        String loadJson = gson.toJson(load);

        sessionLoopAll(gameID, loadJson);

    }
}
