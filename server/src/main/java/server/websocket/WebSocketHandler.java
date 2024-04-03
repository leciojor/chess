package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.requests.RegisterRequest;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    //add connection manager (keeps track with map of users in each session (WITH AUTHTOKEN TO IDENTIFY USER))

    //method for getting and handling the notifications on the server side
    @OnWebSocketMessage 
    public void onMessage(Session session, String msg) throws Exception {
        Gson gson = new Gson();

        UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

        Connection conn = new Connection(command.getAuthString(), session);
        if (conn != null) {
            switch (command.getCommandType()) {
                case JOIN_PLAYER -> join(conn, msg);
                case JOIN_OBSERVER -> observe(conn, msg);
                case MAKE_MOVE -> move(conn, msg);
                case LEAVE -> leave(conn, msg);
                case RESIGN -> resign(conn, msg);
            }
        } else {
            Connection.sendError(session.getRemote(), "unknown user");
        }



    }

    //add websocket endpoints (eg. join, observe, move, leave, resign)

    public void join(Connection conn, String msg){

    }

    public void observe(Connection conn, String msg){

    }

    public void move(Connection conn, String msg){

    }

    public void leave(Connection conn, String msg){

    }

    public void resign(Connection conn, String msg){

    }


}