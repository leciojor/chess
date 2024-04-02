package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    //add connection manager (keeps track with map of users in each session (WITH AUTHTOKEN TO IDENTIFY USER))

    //method for getting and handling the notifications on the server side
    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        UserGameCommand command = readJson(msg, UserGameCommand.class);

        Connection conn = getConnection(command.getAuthString(), session);
        if (conn != null) {
            switch (command.commandType) {
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

    Connection getConnection(String authToken, Session session){
        //use Websocket section class
    }


}