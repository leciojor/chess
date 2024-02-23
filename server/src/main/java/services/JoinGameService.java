package services;


import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.*;
import server.*;

import java.util.Objects;

public class JoinGameService {

    private JoinGameRequest request;

    private UserDAO user = new MemoryUserDAO();

    private AuthDAO auth = new MemoryAuthDAO();

    private GameDAO game = new MemoryGameDAO();



    public JoinGameService(JoinGameRequest request){
        this.request = request;
    }

    public JoinGameResponse joinGame(String color, String gameid, String authtoken){
        AuthData user_data = auth.getCurrentToken();
        GameData game_data = game.getGameByID(gameid);
        if (game_data != null){
            if (Objects.equals(user_data.authToken(), authtoken)){
                if (Objects.equals(color, "WHITE") | Objects.equals(color, "BLACK")){
                    if (Objects.equals(color, "WHITE") && game_data.whiteUsername() == "" ){
                        GameData new_user_game_data = new GameData(Integer.parseInt(gameid), user_data.username(),
                                game_data.blackUsername(), game_data.gameName(), game_data.game());
                        game.addUser(game_data, new_user_game_data);

                        JoinGameResponse response = new JoinGameResponse();
                        response.setStatus(200);
                        return response;
                    }
                    else if (Objects.equals(color, "BLACK") && game_data.blackUsername() == "" ){
                        GameData new_user_game_data = new GameData(Integer.parseInt(gameid), game_data.whiteUsername(),
                                user_data.username(), game_data.gameName(), game_data.game());
                        game.addUser(game_data, new_user_game_data);

                        JoinGameResponse response = new JoinGameResponse();
                        response.setStatus(200);
                        return response;
                    }
                    Err error = new Err(403);
                    return new JoinGameResponse(error);
                }
                else if (gameid == null || authtoken == null){
                    Err error = new Err(400);
                    return new JoinGameResponse(error);
                }
                else{
                    JoinGameResponse response = new JoinGameResponse();
                    response.setStatus(200);
                    return response;
                }
            }
            Err error = new Err(401);
            return new JoinGameResponse(error);

        }
        Err error = new Err(500);
        return new JoinGameResponse(error);


    }

}
