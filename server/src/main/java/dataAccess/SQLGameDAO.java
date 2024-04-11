package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class SQLGameDAO implements GameDAO{
    @Override
    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Game (gameid, whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?, ?)")) {
                String randomToken = UUID.randomUUID().toString();
                preparedStatement.setInt(1, gameID);
                if (Objects.equals(whiteUsername, "")){
                    whiteUsername = null;
                }
                if (Objects.equals(blackUsername, "")){
                    blackUsername = null;
                }
                preparedStatement.setString(2, whiteUsername);
                preparedStatement.setString(3, blackUsername);
                preparedStatement.setString(4, gameName);

                Gson gson = new Gson();
                String serializedGame = gson.toJson(game);

                preparedStatement.setString(5, serializedGame);

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
                        ChessGame deserializedGame = gson.fromJson(game, ChessGame.class);


                        return new GameData(gameID, whiteUsername, blackUsername, gameName, deserializedGame);
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
                String serializedGame = gson.toJson(newGame.game());

                preparedStatement.setString(5, serializedGame);
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
                        ChessGame deserializedGame = gson.fromJson(game, ChessGame.class);


                        return new GameData(gameID, whiteUsername, blackUsername, gameName, deserializedGame);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public HashSet<GameData> getListGames() throws DataAccessException, SQLException {
        HashSet<GameData> currentGames = new HashSet<GameData>();
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
                        ChessGame deserializedGame = gson.fromJson(game, ChessGame.class);

                        GameData finalGame = new GameData(gameid, whiteUsername, blackUsername, gameName, deserializedGame);
                        currentGames.add(finalGame);
                    }
                }
            }
        }
        return currentGames;
    }

    @Override
    public void updateGame(int gameId, ChessGame updatedGame) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("UPDATE Game SET game = ? WHERE gameId = ?")) {
                Gson gson = new Gson();
                String updatedGameString = gson.toJson(updatedGame);

                preparedStatement.setString(1, updatedGameString);
                preparedStatement.setString(2, String.valueOf(gameId));
                preparedStatement.executeUpdate();

            }
        }
    }

    @Override
    public void updateUser(ChessGame.TeamColor color, String newUsername, int gameId) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            if (color == ChessGame.TeamColor.WHITE){
                try (var preparedStatement = conn.prepareStatement("UPDATE Game SET whiteUsername = ? WHERE gameId = ?")) {
                    Gson gson = new Gson();
                    String updatedUsername;

                    if (newUsername != null){
                        updatedUsername = gson.toJson(newUsername);
                        preparedStatement.setString(1, updatedUsername);
                        preparedStatement.setString(2, String.valueOf(gameId));
                        preparedStatement.executeUpdate();
                    }
                    else{
                        preparedStatement.setNull(1, java.sql.Types.VARCHAR);
                        preparedStatement.setString(2, String.valueOf(gameId));
                        preparedStatement.executeUpdate();
                    }

                }
            }
            else{
                try (var preparedStatement = conn.prepareStatement("UPDATE Game SET blackUsername = ? WHERE gameId = ?")) {
                    Gson gson = new Gson();
                    String updatedUsername = gson.toJson(newUsername);

                    if (newUsername != null){
                        updatedUsername = gson.toJson(newUsername);
                        preparedStatement.setString(1, updatedUsername);
                        preparedStatement.setString(2, String.valueOf(gameId));
                        preparedStatement.executeUpdate();
                    }
                    else{
                        preparedStatement.setNull(1, java.sql.Types.VARCHAR);
                        preparedStatement.setString(2, String.valueOf(gameId));
                        preparedStatement.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
