package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;
import java.util.Vector;

public interface GameDAO {

    void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);

    GameData getGame(String gameName);

    void addUser(GameData oldGame, GameData newGame);

    GameData getGameByID(String gameId);

    HashSet<GameData> getListGames();



    void deleteGame();
}
