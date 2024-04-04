package server.websocket;
import org.eclipse.jetty.websocket.api.Session;

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
}
