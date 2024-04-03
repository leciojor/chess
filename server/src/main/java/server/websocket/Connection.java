package server.websocket;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String authToken;
    public Session session;

    public Connection(String authToken, Session session) {
        this.authToken = authToken;
        this.session = session;
    }

    public static void sendError(RemoteEndpoint sessionRemote, String msg) throws IOException {
        sessionRemote.sendString(msg);
    }



}