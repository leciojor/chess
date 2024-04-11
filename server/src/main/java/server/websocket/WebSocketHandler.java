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
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.subMessages.ErrorMessage;
import webSocketMessages.serverMessages.subMessages.LoadGame;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.subCommands.JoinPlayer;
import webSocketMessages.userCommands.subCommands.Leave;
import webSocketMessages.userCommands.subCommands.MakeMove;
import webSocketMessages.userCommands.subCommands.Resign;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    private static ConnectionManager connections = new ConnectionManager();

    private boolean checkEmptyGame(int gameID, ChessGame.TeamColor userColor, String username) throws SQLException, DataAccessException {
        if (userColor == ChessGame.TeamColor.WHITE && !Objects.equals(getWhiteUsername(gameID), username)){
            return true;
        }
        if (userColor == ChessGame.TeamColor.BLACK && !Objects.equals(getBlackUsername(gameID), username)){
            return true;
        }
        return false;
    }

    private void sendError(Connection conn, String errorMessage) throws IOException {
        Gson gson = new Gson();

        ErrorMessage error = new ErrorMessage(errorMessage, ServerMessage.ServerMessageType.ERROR);
        String errorJson = gson.toJson(error);
        conn.session.getRemote().sendString(errorJson);

    }

    private void checkStalCases(SQLGameDAO gameSql, ChessGame game, Connection conn, String whiteUsername, String blackUsername, int gameID) throws IOException, SQLException, DataAccessException {
        if (game.isInCheck(ChessGame.TeamColor.WHITE)){
            connections.sendNotifications(gameID, whiteUsername + " is in Check", conn.session, true);
        }

        if (game.isInCheck(ChessGame.TeamColor.BLACK)){
            connections.sendNotifications(gameID, blackUsername + " is in Check", conn.session, true);
        }

        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)){
            game.setIsOver(true);
            gameSql.updateGame(gameID, game);
            connections.sendNotifications(gameID, whiteUsername + " is in Check Mate", conn.session, true);
        }

        if (game.isInCheckmate(ChessGame.TeamColor.BLACK)){
            game.setIsOver(true);
            gameSql.updateGame(gameID, game);
            connections.sendNotifications(gameID, blackUsername + " is in Check Mate", conn.session, true);
        }

        if (game.isInStalemate(ChessGame.TeamColor.WHITE)){

            game.setIsOver(true);
            gameSql.updateGame(gameID, game);
            connections.sendNotifications(gameID, whiteUsername + " is in StallMate", conn.session, true);
        }

        if (game.isInStalemate(ChessGame.TeamColor.BLACK)){

            game.setIsOver(true);
            gameSql.updateGame(gameID, game);
            connections.sendNotifications(gameID, blackUsername + " is in StallMate", conn.session, true);
        }
    }


    private String getAuthToken(UserGameCommand command) throws SQLException, DataAccessException {
        SQLAuthDAO auth = new SQLAuthDAO();
        AuthData authData = auth.getCurrentToken(command.getAuthString());
        if (authData == null){
            return null;
        }
        return authData.authToken();
    }

    private String getUsername(UserGameCommand command) throws SQLException, DataAccessException {
        SQLAuthDAO auth = new SQLAuthDAO();
        AuthData authData = auth.getCurrentToken(command.getAuthString());
        if (authData == null){
            return null;
        }
        return authData.username();
    }

    private ChessGame getChessGame(int gameID) throws SQLException, DataAccessException {
        SQLGameDAO game = new SQLGameDAO();
        GameData gameData = game.getGameByID(String.valueOf(gameID));
        if (gameData == null){
            return null;
        }
        return gameData.game();

    }


    private String getWhiteUsername(int gameID) throws SQLException, DataAccessException {
        SQLGameDAO game = new SQLGameDAO();
        GameData gameData = game.getGameByID(String.valueOf(gameID));
        if (gameData == null){
            return null;
        }
        return gameData.whiteUsername();

    }

    private String getBlackUsername(int gameID) throws SQLException, DataAccessException {
        SQLGameDAO game = new SQLGameDAO();
        GameData gameData = game.getGameByID(String.valueOf(gameID));
        if (gameData == null){
            return null;
        }
        return gameData.blackUsername();

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

        JoinPlayer joinCommand = gson.fromJson(msg, JoinPlayer.class);
        connections.add(joinCommand.getGameID(), conn.session);

        int gameID = joinCommand.getGameID();
        String whiteUsername = getWhiteUsername(gameID);
        String blackUsername = getBlackUsername(gameID);
        String username = getUsername(joinCommand);

        if((joinCommand.getPlayerColor() == ChessGame.TeamColor.WHITE && whiteUsername != null && !whiteUsername.equals(username))){
            sendError(conn, "SPOT ALREADY HAS USER " + whiteUsername);
            return;
        }

        else if((joinCommand.getPlayerColor() == ChessGame.TeamColor.BLACK && blackUsername != null && !blackUsername.equals(username))){
            sendError(conn, "SPOT ALREADY HAS USER " + blackUsername);
            return;
        }

        ChessGame game = getChessGame(gameID);

        if (game == null || checkEmptyGame(gameID, joinCommand.getPlayerColor(), username)){
            sendError(conn, "Game does not exist or is empty");
            return;
        }

        //send ServerMessageObject (JSON TEXT)

        connections.sendNotifications(joinCommand.getGameID(), username + " joined game as " + joinCommand.getPlayerColor(), conn.session, false);

        //sending loadgame message to added user
        LoadGame gameCommand = new LoadGame(game, ServerMessage.ServerMessageType.LOAD_GAME);
        String gameJson = gson.toJson(gameCommand);

        conn.session.getRemote().sendString(gameJson);

    }

    public void observe(Connection conn, String msg) throws IOException, SQLException, DataAccessException {
        Gson gson = new Gson();
        JoinPlayer joinCommand = gson.fromJson(msg, JoinPlayer.class);
        connections.add(joinCommand.getGameID(), conn.session);
        //send ServerMessageObject (JSON TEXT)

        int gameID = joinCommand.getGameID();
        String username = getUsername(joinCommand);

        ChessGame game = getChessGame(gameID);

        if (game == null){
            sendError(conn, "Game does not exist or is empty");
            return;
        }

        if (!Objects.equals(getAuthToken(joinCommand), joinCommand.getAuthString())){
            sendError(conn, "Wrong AuthToken");
            return;
        }

        connections.sendNotifications(joinCommand.getGameID(), username + " joined game as observer", conn.session, false);

        //sending loadgame message to added user
        LoadGame gameCommand = new LoadGame(getChessGame(joinCommand.getGameID()), ServerMessage.ServerMessageType.LOAD_GAME);
        String gameJson = gson.toJson(gameCommand);

        conn.session.getRemote().sendString(gameJson);
    }

    public void move(Connection conn, String msg) throws IOException, SQLException, DataAccessException, InvalidMoveException {
        Gson gson = new Gson();
        SQLGameDAO gameSql = new SQLGameDAO();
        ChessGame.TeamColor userColor = null;


        MakeMove moveCommand = gson.fromJson(msg, MakeMove.class);
        ChessMove move = moveCommand.getMove();
        int gameID = moveCommand.getGameID();
        ChessGame game = getChessGame(gameID);
        String whiteUsername = getWhiteUsername(gameID);
        String blackUsername = getBlackUsername(gameID);

        //getting user color
        if (Objects.equals(getWhiteUsername(gameID), getUsername(moveCommand))) {
            userColor = ChessGame.TeamColor.WHITE;
        }
        else if (Objects.equals(getBlackUsername(gameID), getUsername(moveCommand))){
            userColor = ChessGame.TeamColor.BLACK;
        }
        //Verifying move validity

        if (userColor == null){
            sendError(conn, "YOU ARE JUST AN OBSERVER, NO MOVEMENT ALLOWED");
            return;
        }


        else if (game.getTeamTurn() != userColor){
            sendError(conn, "NOT YOUR TURN");
            return;
        }

        else if (game.getIsOver()){
            sendError(conn, "GAME IS OVER, NO MOVEMENTS ALLOWED");
            return;
        }

        //Updating game

        try{
            game.makeMove(move);
        }
        catch (InvalidMoveException e){
            sendError(conn, "INVALID MOVE");
            return;
        }

        gameSql.updateGame(gameID, game);

        //Sending load_game message

        connections.sendLoad(getChessGame(gameID), gameID);

        //Sending notifications
        String username = getUsername(moveCommand);

        connections.sendNotifications(gameID, username + " moved from " + move.getStartPosition().toString() + " to " + move.getEndPosition().toString(), conn.session, false);

        //checking check/stal cases
        checkStalCases(gameSql, game, conn, whiteUsername, blackUsername, gameID);

    }

    public void leave(Connection conn, String msg) throws IOException, SQLException, DataAccessException {
        Gson gson = new Gson();
        Leave leaveCommand = gson.fromJson(msg, Leave.class);

        //removing from connections
        int gameID = leaveCommand.getGameID();
        connections.remove(gameID, conn.session);

        ChessGame game = getChessGame(gameID);

        //updating game
        String username = getUsername(leaveCommand);
        SQLGameDAO gameSql = new SQLGameDAO();

        ChessGame.TeamColor color;

        if (Objects.equals(getWhiteUsername(gameID), username)){
            color = ChessGame.TeamColor.WHITE;
        }
        else{
            color = ChessGame.TeamColor.BLACK;
        }

        gameSql.updateUser(color, null, gameID);

        //sending notifications

        connections.sendNotifications(gameID, username + " left", conn.session, false);
    }

    public void resign(Connection conn, String msg) throws IOException, SQLException, DataAccessException {
        Gson gson = new Gson();
        SQLGameDAO gameSql = new SQLGameDAO();

        Resign resignCommand = gson.fromJson(msg, Resign.class);
        int gameID = resignCommand.getGameID();
        ChessGame game = getChessGame(gameID);

        //setting up error scenarios

        if (!Objects.equals(getWhiteUsername(gameID), getUsername(resignCommand)) && !Objects.equals(getBlackUsername(gameID), getUsername(resignCommand))){
            sendError(conn, "OBSERVERS ARE NOT ALLOWED TO RESIGN");
            return;
        }

        else if (game.getIsOver()){
            sendError(conn, "ANOTHER PLAYER ALREADY RESIGNED, GAME IS OVER");
            return;
        }

        //updating game
        game.setIsOver(true);
        gameSql.updateGame(gameID, game);

        //sending notifications
        String  username = getUsername(resignCommand);
        connections.sendNotifications(gameID, username + " resigned. The game is over", conn.session, true);

    }


}