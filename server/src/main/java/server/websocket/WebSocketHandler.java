package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.requests.RegisterRequest;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.subMessages.LoadGame;
import webSocketMessages.serverMessages.subMessages.Notification;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.subCommands.JoinPlayer;


import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private static ConnectionManager connections = new ConnectionManager();

    //method for getting and handling the notifications on the server side
    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        Gson gson = new Gson();

        UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

        Connection conn = new Connection(command.getAuthString(), session);
        if (command.getAuthString() != null) {
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

    //send a SERVER MESSAGE to the client using ON MESSAGE
    public void join(Connection conn, String msg) throws IOException {
        Gson gson = new Gson();
        //see slides for general on message for more instruction
        //send to all clients in session when necessary
        //desirialize msg to COMMANDS instance (WHERE MESSAGE IS USED)
        //send messages to all necessary clients
        //change the connection manager to keep track of it all

        JoinPlayer join_command = gson.fromJson(msg, JoinPlayer.class);
        connections.add(join_command.getGameID(), conn.session);
        //send ServerMessageObject (JSON TEXT)

        connections.sendNotifications(join_command.getGameID(), "joined game as");

        //sending loadgame message to added user
        LoadGame game = new LoadGame(join_command.getGameID(), ServerMessage.ServerMessageType.LOAD_GAME);
        String game_json = gson.toJson(game);

        conn.session.getRemote().sendString(game_json);

    }

    public void observe(Connection conn, String msg) throws IOException {

    }

    public void move(Connection conn, String msg) throws IOException {

    }

    public void leave(Connection conn, String msg) throws IOException {

    }

    public void resign(Connection conn, String msg) throws IOException {

    }


}