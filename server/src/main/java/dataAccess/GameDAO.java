package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Vector;

public interface GameDAO {

    void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);

    GameData getGame(String game_name);

    Vector<GameData> getListGames();

    void updateGame();

    void deleteGame();
}
