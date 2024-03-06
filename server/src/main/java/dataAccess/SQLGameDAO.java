package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;

public class SQLGameDAO implements GameDAO{
    @Override
    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    }

    @Override
    public GameData getGame(String gameName) {
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
