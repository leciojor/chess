package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Vector;

public interface GameDAO {

    void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game);

    GameData getGame(String game_name);

    void addUser(GameData old_game, GameData new_game);

    GameData getGameByID(String game_Id);

    Vector<GameData> getListGames();

    void updateGame();

    void deleteGame();
}
