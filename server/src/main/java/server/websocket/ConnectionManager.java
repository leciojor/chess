package server.websocket;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.subMessages.Notification;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ConnectionManager {

//gameId by a list with the conncetions
    private static final HashMap<Integer, List<Session>> connections = new HashMap<>();

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

    public void sendNotifications(int gameID, String msg) throws IOException {
        Gson gson = new Gson();

        Notification notification = new Notification(msg, ServerMessage.ServerMessageType.NOTIFICATION);
        String notification_json = gson.toJson(notification);

        List<Session> game_sessions = connections.get(gameID);

        for (Session session : game_sessions){
            session.getRemote().sendString(notification_json);
        }

    }
}
