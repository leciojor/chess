package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import server.requests.RegisterRequest;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;

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
                    if (!rs.next()){
                        return null;
                    }
                    while (rs.next()) {
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
    public void addUser(GameData oldGame, GameData newGame) {
        
    }

    @Override
    public GameData getGameByID(String gameId) {
        return null;
    }

    @Override
    public HashSet<GameData> getListGames() {
        return null;
    }

    @Override
    public void deleteGame() {

    }
}
