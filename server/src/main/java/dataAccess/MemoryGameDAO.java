package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.Objects;
import java.util.Vector;

public class MemoryGameDAO implements GameDAO{

    private static Vector<GameData> game_list = new Vector<GameData>();

    @Override
    public void createGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        GameData game_ = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        game_list.add(game_);
    }


    @Override
    public GameData getGame(String game_name) {
        for (int i = 0; i < game_list.size(); i++){
            if (Objects.equals(game_list.get(i).gameName(), game_name)){
                return game_list.get(i);
            }
        }

        return null;
    }

    @Override
    public Vector<GameData> getListGames() {
        return game_list;
    }

    @Override
    public void updateGame() {

    }

    @Override
    public void deleteGame() {

    }
}
