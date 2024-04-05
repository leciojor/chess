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
    private void sessionLoop(int gameID, String json, Session user_session) throws IOException {
        List<Session> game_sessions = connections.get(gameID);

        for (Session session : game_sessions){

            if (session != user_session){
                session.getRemote().sendString(json);
            }

        }
    }

    private void sessionLoopAll(int gameID, String json) throws IOException {
        List<Session> game_sessions = connections.get(gameID);

        for (Session session : game_sessions){
            session.getRemote().sendString(json);
        }
    }

    public void add(int gameID, Session session){

        if (connections.containsKey(gameID)){
            List<Session> game_connections = connections.get(gameID);
            game_connections.add(session);
        }
        else{
            List<Session> game_connections = new ArrayList<>();
            game_connections.add(session);
            connections.put(gameID, game_connections);
        }

    }

    public void remove(int gameID, Session session){
        if (connections.containsKey(gameID)){
            List<Session> game_connections = connections.get(gameID);
            game_connections.remove(session);
        }

    }


    public void removeAll(int gameID) {
        if (connections.containsKey(gameID)){
            List<Session> game_sessions = connections.get(gameID);
            for (Session session : game_sessions){
                game_sessions.remove(session);
            }
        }
    }

    public void sendNotifications(int gameID, String msg, Session user_session, boolean all) throws IOException {
        Gson gson = new Gson();
        //sessionLoop may not be excluding the current user (REVIEW LOGIC)
        Notification notification = new Notification(msg, ServerMessage.ServerMessageType.NOTIFICATION);
        String notification_json = gson.toJson(notification);
        if (all){
            sessionLoopAll(gameID, notification_json);
        }
        else{
            sessionLoop(gameID, notification_json, user_session);
        }



    }

    public void sendLoad(ChessGame game, int gameID) throws IOException{
        Gson gson = new Gson();

        LoadGame load = new LoadGame(game, ServerMessage.ServerMessageType.LOAD_GAME);
        String load_json = gson.toJson(load);

        sessionLoopAll(gameID, load_json);

    }
}
