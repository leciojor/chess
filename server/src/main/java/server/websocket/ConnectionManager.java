package server.websocket;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.subMessages.LoadGame;
import webSocketMessages.serverMessages.subMessages.Notification;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ConnectionManager {

//gameId by a list with the conncetions
    private static final HashMap<Integer, List<Session>> connections = new HashMap<>();

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
        List<Session> game_connections = connections.get(gameID);
        game_connections.add(session);
    }

    public void remove(int gameID, Session session){
        List<Session> game_connections = connections.get(gameID);
        game_connections.remove(session);
    }


    public void removeAll(int gameID) {
        List<Session> game_sessions = connections.get(gameID);
        for (Session session : game_sessions){
            game_sessions.remove(session);
        }
    }

    public void sendNotifications(int gameID, String msg, Session user_session, boolean all) throws IOException {
        Gson gson = new Gson();

        Notification notification = new Notification(msg, ServerMessage.ServerMessageType.NOTIFICATION);
        String notification_json = gson.toJson(notification);
        if (all){
            sessionLoopAll(gameID, notification_json);
        }
        else{
            sessionLoop(gameID, notification_json, user_session);
        }



    }

    public void sendLoad(int gameID) throws IOException{
        Gson gson = new Gson();

        LoadGame load = new LoadGame(gameID, ServerMessage.ServerMessageType.NOTIFICATION);
        String load_json = gson.toJson(load);

        sessionLoopAll(gameID, load_json);

    }
}
