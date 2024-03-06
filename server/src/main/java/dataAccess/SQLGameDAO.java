package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import server.requests.RegisterRequest;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;
import java.util.Vector;

public class SQLGameDAO implements GameDAO{
    @Override
    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Game (gameid, whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?, ?)")) {
                String randomToken = UUID.randomUUID().toString();
                preparedStatement.setInt(1, gameID);
                if (whiteUsername == ""){
                    whiteUsername = null;
                }
                if (blackUsername == ""){
                    blackUsername = null;
                }
                preparedStatement.setString(2, whiteUsername);
                preparedStatement.setString(3, blackUsername);
                preparedStatement.setString(4, gameName);

                Gson gson = new Gson();
                String serialized_game = gson.toJson(game);

                preparedStatement.setString(5, serialized_game);

                preparedStatement.executeUpdate();

            }

        }
    }

    @Override
    public GameData getGame(String gameName) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT gameid, whiteUsername, blackUsername, gameName, game FROM Game WHERE gameName=?")) {
                preparedStatement.setString(1, gameName);
                try (var rs = preparedStatement.executeQuery()) {

                    if (rs.next()) {
                        var gameID = rs.getInt("gameid");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var game = rs.getString("game");

                        Gson gson = new Gson();
                        ChessGame deserialized_game = gson.fromJson(game, ChessGame.class);


                        return new GameData(gameID, whiteUsername, blackUsername, gameName, deserialized_game);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void addUser(GameData oldGame, GameData newGame) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("UPDATE Game SET gameid=?, whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameid=?")) {
                preparedStatement.setInt(1, newGame.gameID());
                preparedStatement.setString(2, newGame.whiteUsername());
                preparedStatement.setString(3, newGame.blackUsername());
                preparedStatement.setString(4, newGame.gameName());

                Gson gson = new Gson();
                String serialized_game = gson.toJson(newGame.game());

                preparedStatement.setString(5, serialized_game);
                preparedStatement.setInt(6, oldGame.gameID());

                preparedStatement.executeUpdate();
            }

        }
    }

    @Override
    public GameData getGameByID(String gameId) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT gameid, whiteUsername, blackUsername, gameName, game FROM Game WHERE gameid=?")) {
                preparedStatement.setInt(1, Integer.parseInt(gameId));
                try (var rs = preparedStatement.executeQuery()) {

                    if (rs.next()) {
                        var gameID = rs.getInt("gameid");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var game = rs.getString("game");

                        Gson gson = new Gson();
                        ChessGame deserialized_game = gson.fromJson(game, ChessGame.class);


                        return new GameData(gameID, whiteUsername, blackUsername, gameName, deserialized_game);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public HashSet<GameData> getListGames() throws DataAccessException, SQLException {
        HashSet<GameData> current_games = new HashSet<GameData>();
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT gameid, whiteUsername, blackUsername, gameName, game FROM Game")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var gameid = rs.getInt("gameid");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var game = rs.getString("game");

                        Gson gson = new Gson();
                        ChessGame deserialized_game = gson.fromJson(game, ChessGame.class);

                        GameData final_game = new GameData(gameid, whiteUsername, blackUsername, gameName, deserialized_game);
                        current_games.add(final_game);
                    }
                }
            }
        }
        return current_games;
    }

    @Override
    public void deleteGame() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM Game")) {
                preparedStatement.executeUpdate();
            }
        }
    }
}
