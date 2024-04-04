package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO{

    private static HashSet<GameData> gameList = new HashSet<GameData>();

    @Override
    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame chessGame) {
        if (whiteUsername == ""){
            whiteUsername = null;
        }
        if (blackUsername == ""){
            blackUsername = null;
        }
        GameData game = new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
        gameList.add(game);
    }


    @Override
    public GameData getGame(String gameName) {
        if (gameList != null){;
            for (GameData game : gameList){
                if (Objects.equals(game.gameName(), gameName)){
                    return game;
                }
            }
        }

        return null;
    }

    @Override
    public void addUser(GameData oldGame, GameData newGame) {
        gameList.remove(oldGame);
        gameList.add(newGame);
    }

    @Override
    public GameData getGameByID(String gameId) {
        if (gameList != null & gameId != null){
            for (GameData game : gameList){
                if (Objects.equals(game.gameID(), Integer.parseInt(gameId))){
                    return game;
                }
            }
        }

        return null;
    }

    @Override
    public HashSet<GameData> getListGames() {
        return gameList;
    }

    @Override
    public void updateGame(int gameId, ChessGame UpdatedGame) throws DataAccessException, SQLException {

    }

    @Override
    public void updateUser(ChessGame.TeamColor color, String newUsername, int gameId) throws DataAccessException {

    }


    @Override
    public void deleteGame() {
        gameList = new HashSet<GameData>();
    }
}
