package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.HashSet;
import java.util.Objects;
import java.util.Vector;

public class MemoryGameDAO implements GameDAO{

    private static HashSet<GameData> game_list = new HashSet<GameData>();

    @Override
    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        if (whiteUsername == ""){
            whiteUsername = null;
        }
        if (blackUsername == ""){
            blackUsername = null;
        }
        GameData game_ = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        game_list.add(game_);
    }


    @Override
    public GameData getGame(String game_name) {
        if (game_list != null){;
            for (GameData game : game_list){
                if (Objects.equals(game.gameName(), game_name)){
                    return game;
                }
            }
        }

        return null;
    }

    @Override
    public void addUser(GameData old_game, GameData new_game) {
        game_list.remove(old_game);
        game_list.add(new_game);
    }

    @Override
    public GameData getGameByID(String game_Id) {
        if (game_list != null & game_Id != null){
            for (GameData game : game_list){
                if (Objects.equals(game.gameID(), Integer.parseInt(game_Id))){
                    return game;
                }
            }
        }

        return null;
    }

    @Override
    public HashSet<GameData> getListGames() {
        return game_list;
    }

    @Override
    public void updateGame() {

    }

    @Override
    public void deleteGame() {
        game_list = new HashSet<GameData>();
    }
}
