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
import server.Server;
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

    private void sendError(Connection conn, String errorMessage) throws IOException {
        Gson gson = new Gson();

        ErrorMessage error = new ErrorMessage(errorMessage, ServerMessage.ServerMessageType.ERROR);
        String error_json = gson.toJson(error);
        conn.session.getRemote().sendString(error_json);
        return;
    }

    private String getUsername(UserGameCommand command) throws SQLException, DataAccessException {
        SQLAuthDAO auth = new SQLAuthDAO();
        AuthData auth_data = auth.getCurrentToken(command.getAuthString());
        if (auth_data == null){
            return null;
        }
        return auth_data.username();
    }

    private ChessGame getChessGame(int gameID) throws SQLException, DataAccessException {
        SQLGameDAO game = new SQLGameDAO();
        GameData game_data = game.getGameByID(String.valueOf(gameID));
        if (game_data == null){
            return null;
        }
        return game_data.game();

    }


    private String getWhiteUsername(int gameID) throws SQLException, DataAccessException {
        SQLGameDAO game = new SQLGameDAO();
        GameData game_data = game.getGameByID(String.valueOf(gameID));
        if (game_data == null){
            return null;
        }
        return game_data.whiteUsername();

    }

    private String getBlackUsername(int gameID) throws SQLException, DataAccessException {
        SQLGameDAO game = new SQLGameDAO();
        GameData game_data = game.getGameByID(String.valueOf(gameID));
        if (game_data == null){
            return null;
        }
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
            sendError(conn, "unknown user");
        }



    }


    public void join(Connection conn, String msg) throws IOException, SQLException, DataAccessException {
        ErrorMessage error = null;
        Gson gson = new Gson();

        JoinPlayer join_command = gson.fromJson(msg, JoinPlayer.class);
        connections.add(join_command.getGameID(), conn.session);


        if (Server.returned_error) {

            sendError(conn, "SERVER ERROR");
        }
        else if(!Objects.equals(getUsername(join_command), getBlackUsername(join_command.getGameID())) && !Objects.equals(getUsername(join_command), getWhiteUsername(join_command.getGameID()))){
            sendError(conn, "SPOT ALREADY HAS USER " + getWhiteUsername(join_command.getGameID()));
            return;
        }
        
        //send ServerMessageObject (JSON TEXT)

        String username = getUsername(join_command);

        connections.sendNotifications(join_command.getGameID(), username + " joined game as " + join_command.getPlayerColor(), conn.session, false);

        //sending loadgame message to added user
        LoadGame game = new LoadGame(getChessGame(join_command.getGameID()), ServerMessage.ServerMessageType.LOAD_GAME);
        String game_json = gson.toJson(game);

        conn.session.getRemote().sendString(game_json);

    }

    public void observe(Connection conn, String msg) throws IOException, SQLException, DataAccessException {
        Gson gson = new Gson();
        JoinPlayer join_command = gson.fromJson(msg, JoinPlayer.class);
        connections.add(join_command.getGameID(), conn.session);
        //send ServerMessageObject (JSON TEXT)

        if (getChessGame(join_command.getGameID()) == null){
            sendError(conn, "Game does not exist");
            return;
        }

        else if (Server.returned_error){
            sendError(conn, "SERVER ERROR");
            return;
        }

        String username = getUsername(join_command);

        connections.sendNotifications(join_command.getGameID(), username + " joined game as observer", conn.session, false);

        //sending loadgame message to added user
        LoadGame game = new LoadGame(getChessGame(join_command.getGameID()), ServerMessage.ServerMessageType.LOAD_GAME);
        String game_json = gson.toJson(game);

        conn.session.getRemote().sendString(game_json);
    }

    public void move(Connection conn, String msg) throws IOException, SQLException, DataAccessException, InvalidMoveException {
        Gson gson = new Gson();
        SQLGameDAO game_sql = new SQLGameDAO();
        ChessGame.TeamColor user_color = null;

        MakeMove move_command = gson.fromJson(msg, MakeMove.class);
        int gameID = move_command.getGameID();
        ChessMove move = move_command.getMove();

        //getting user color
        if (Objects.equals(getWhiteUsername(gameID), getUsername(move_command))) {
            user_color = ChessGame.TeamColor.WHITE;
        }
        else if (Objects.equals(getBlackUsername(gameID), getUsername(move_command))){
            user_color = ChessGame.TeamColor.BLACK;
        }
        //Verifying move validity

        ChessGame game = getChessGame(gameID);

        if (game.getTeamTurn() != user_color){
            sendError(conn, "IT IS NOT YOUR TURN");
            return;
        }

        //try catch not working (not giving exception)
        try{
            game.makeMove(move);

        }
        catch (InvalidMoveException e){
            sendError(conn, "INVALID MOVE");
            return;
        }

        if (game.getTeamTurn() != user_color){
            sendError(conn, "IT IS NOT YOUR TURN");
            return;
        }

        //making normal move and invalid move fail (they get into it) - STALMATE CONDITION FOR MAKE MOVE NORMAL
        else if (game.isInCheckmate(ChessGame.TeamColor.BLACK) || game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK) || game.isInStalemate(ChessGame.TeamColor.WHITE)){
            sendError(conn, "GAME IS OVER, NO MOVES ARE ALLOWED");
            return;
        }



        //Updating game

        game_sql.updateGame(gameID, game);

        //Sending load_game message

        connections.sendLoad(getChessGame(gameID), gameID);

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
        int gameID = resign_command.getGameID();
        ChessGame game = getChessGame(gameID);


        //removing from connections

//        connections.removeAll(gameID);


        //setting up error scenarios

        if (!Objects.equals(getWhiteUsername(gameID), getUsername(resign_command)) && !Objects.equals(getBlackUsername(gameID), getUsername(resign_command))){
            sendError(conn, "OBSERVERS ARE NOT ALLOWED TO RESIGN");
            return;
        }

        else if (game.getIsOver()){
            sendError(conn, "ANOTHER PLAYER ALREADY RESIGNED, GAME IS OVER");
            return;
        }

        //updating game
        //may need to change this logic (so no more moves are possible and the game is over) - consider isInStallMate or IsINCheckMate from chessGame
        game.setIsOver(true);

        game_sql.updateGame(gameID, game);

        //sending notifications
        String  username = getUsername(resign_command);
        connections.sendNotifications(gameID, username + " resigned. The game is over", conn.session, true);

    }


}