package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.requests.RegisterRequest;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.subMessages.ErrorMessage;
import webSocketMessages.serverMessages.subMessages.LoadGame;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;
import webSocketMessages.userCommands.subCommands.*;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private static ConnectionManager connections = new ConnectionManager();

    private String getUsername(UserGameCommand command) throws SQLException, DataAccessException {
        SQLAuthDAO auth = new SQLAuthDAO();
        AuthData auth_data = auth.getCurrentToken(command.getAuthString());
        return auth_data.username();
    }

    private ChessGame getChessGame(int gameID) throws SQLException, DataAccessException {
        SQLGameDAO game = new SQLGameDAO();
        GameData game_data = game.getGameByID(String.valueOf(gameID));
        return game_data.game();

    }

    private String getWhiteUsername(int gameID) throws SQLException, DataAccessException {
        SQLGameDAO game = new SQLGameDAO();
        GameData game_data = game.getGameByID(String.valueOf(gameID));
        return game_data.whiteUsername();

    }

    private String getBlackUsername(int gameID) throws SQLException, DataAccessException {
        SQLGameDAO game = new SQLGameDAO();
        GameData game_data = game.getGameByID(String.valueOf(gameID));
        return game_data.blackUsername();

    }

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
    public void join(Connection conn, String msg) throws IOException, SQLException, DataAccessException {
        Gson gson = new Gson();
        //see slides for general on message for more instruction
        //send to all clients in session when necessary
        //desirialize msg to COMMANDS instance (WHERE MESSAGE IS USED)
        //send messages to all necessary clients
        //change the connection manager to keep track of it all

        JoinPlayer join_command = gson.fromJson(msg, JoinPlayer.class);
        connections.add(join_command.getGameID(), conn.session);
        //send ServerMessageObject (JSON TEXT)

        String username = getUsername(join_command);

        connections.sendNotifications(join_command.getGameID(), username + " joined game as " + join_command.getPlayerColor(), conn.session, false);

        //sending loadgame message to added user
        LoadGame game = new LoadGame(join_command.getGameID(), ServerMessage.ServerMessageType.LOAD_GAME);
        String game_json = gson.toJson(game);

        conn.session.getRemote().sendString(game_json);

    }

    public void observe(Connection conn, String msg) throws IOException, SQLException, DataAccessException {
        Gson gson = new Gson();
        JoinPlayer join_command = gson.fromJson(msg, JoinPlayer.class);
        connections.add(join_command.getGameID(), conn.session);
        //send ServerMessageObject (JSON TEXT)

        String username = getUsername(join_command);

        connections.sendNotifications(join_command.getGameID(), username + " joined game as observer", conn.session, false);

        //sending loadgame message to added user
        LoadGame game = new LoadGame(join_command.getGameID(), ServerMessage.ServerMessageType.LOAD_GAME);
        String game_json = gson.toJson(game);

        conn.session.getRemote().sendString(game_json);
    }

    public void move(Connection conn, String msg) throws IOException, SQLException, DataAccessException, InvalidMoveException {
        Gson gson = new Gson();
        SQLGameDAO game_sql = new SQLGameDAO();

        MakeMove move_command = gson.fromJson(msg, MakeMove.class);
        int gameID = move_command.getGameID();
        ChessMove move = move_command.getMove();

        ChessGame game = getChessGame(gameID);
        //Verifying move validity
        try{
            game.makeMove(move);
        }
        catch (InvalidMoveException e){
            ErrorMessage error = new ErrorMessage("INVALID MOVE", ServerMessage.ServerMessageType.ERROR);
            String error_json = gson.toJson(error);
            conn.session.getRemote().sendString(error_json);
            return;
        }

        //Updating game

        game_sql.updateGame(gameID, game);

        //Sending load_game message

        connections.sendLoad(gameID);

        //Sending notifications
        String username = getUsername(move_command);

        connections.sendNotifications(gameID, username + "moved from " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString(), conn.session, false);

        //sending checkMate/check notifications
        String white_username = getWhiteUsername(gameID);
        String black_username = getBlackUsername(gameID);

        if (game.isInCheck(ChessGame.TeamColor.WHITE)){
            connections.sendNotifications(gameID, white_username + " is in Check", conn.session, true);
        }

        if (game.isInCheck(ChessGame.TeamColor.BLACK)){
            connections.sendNotifications(gameID, black_username + " is in Check", conn.session, true);
        }

        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)){
            connections.sendNotifications(gameID, white_username + " is in Check Mate", conn.session, true);
        }

        if (game.isInCheckmate(ChessGame.TeamColor.BLACK)){
            connections.sendNotifications(gameID, black_username + " is in Check Mate", conn.session, true);
        }
    }

    public void leave(Connection conn, String msg) throws IOException, SQLException, DataAccessException {
        Gson gson = new Gson();
        Leave leave_command = gson.fromJson(msg, Leave.class);

        //removing from connections
        int gameID = leave_command.getGameID();
        connections.remove(gameID, conn.session);

       ChessGame game = getChessGame(gameID);

       //updating game
        String username = getUsername(leave_command);
        SQLGameDAO game_sql = new SQLGameDAO();

        ChessGame.TeamColor color;

        if (Objects.equals(getWhiteUsername(gameID), username)){
            color = ChessGame.TeamColor.WHITE;
        }
        else{
            color = ChessGame.TeamColor.BLACK;
        }

        game_sql.updateUser(color, username, gameID);

        //sending notifications

        connections.sendNotifications(gameID, username + " left", conn.session, false);
    }

    public void resign(Connection conn, String msg) throws IOException, SQLException, DataAccessException {
        Gson gson = new Gson();
        SQLGameDAO game_sql = new SQLGameDAO();

        Resign resign_command = gson.fromJson(msg, Resign.class);

        //removing from connections
        int gameID = resign_command.getGameID();
        connections.removeAll(gameID);

        //updating game
        ChessGame game = getChessGame(gameID);
        //may need to change this logic (so no more moves are possible and the game is over)
        game.setTeamTurn(null);

        game_sql.updateGame(gameID, game);

        //sending notifications
        String  username = getUsername(resign_command);
        connections.sendNotifications(gameID, username + " resigned. The game is over", conn.session, true);

    }


}