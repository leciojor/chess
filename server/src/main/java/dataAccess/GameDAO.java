package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Vector;

public interface GameDAO {

    void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException, SQLException;

    GameData getGame(String gameName) throws DataAccessException, SQLException;

    void addUser(GameData oldGame, GameData newGame) throws DataAccessException, SQLException;

    GameData getGameByID(String gameId) throws DataAccessException, SQLException;

    HashSet<GameData> getListGames() throws DataAccessException, SQLException;

    void updateGame(int gameId, ChessGame UpdatedGame) throws DataAccessException, SQLException;

    void updateUser(ChessGame.TeamColor color, String newUsername, int gameId) throws DataAccessException;

    void deleteGame() throws DataAccessException, SQLException;
}
