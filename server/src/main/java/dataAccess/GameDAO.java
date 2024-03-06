package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Vector;

public interface GameDAO {

    void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException, SQLException;

    GameData getGame(String gameName) throws DataAccessException, SQLException;

    void addUser(GameData oldGame, GameData newGame);

    GameData getGameByID(String gameId);

    HashSet<GameData> getListGames();



    void deleteGame();
}
