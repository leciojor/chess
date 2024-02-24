package dataAccess;

import chess.ChessGame;
import model.GameData;

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
    public GameData getGame(String game_name) {
        if (gameList != null){;
            for (GameData game : gameList){
                if (Objects.equals(game.gameName(), game_name)){
                    return game;
                }
            }
        }

        return null;
    }

    @Override
    public void addUser(GameData old_game, GameData new_game) {
        gameList.remove(old_game);
        gameList.add(new_game);
    }

    @Override
    public GameData getGameByID(String game_Id) {
        if (gameList != null & game_Id != null){
            for (GameData game : gameList){
                if (Objects.equals(game.gameID(), Integer.parseInt(game_Id))){
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
    public void deleteGame() {
        gameList = new HashSet<GameData>();
    }
}
