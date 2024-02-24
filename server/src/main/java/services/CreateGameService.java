package services;


import dataAccess.*;
import model.*;
import server.*;

import java.util.Objects;

import java.util.Random;
import java.util.Vector;

import chess.*;


public class CreateGameService {

    private final CreateGameRequest request;

    private UserDAO user = new MemoryUserDAO();

    private AuthDAO auth = new MemoryAuthDAO();

    private GameDAO game = new MemoryGameDAO();

    private Vector<Integer> random_ids = new Vector<Integer>();

    public CreateGameService(CreateGameRequest req){
        this.request = req;

    }

    public CreateGameResponse createGame(String game_name, String current_token){
        GameData game_data = game.getGame(game_name);
        AuthData user_data = auth.getCurrentToken();

        if (game_data == null & user_data != null){
                Random random = new Random();
                int random_number = random.nextInt(9000) + 1000;
                while (random_ids.contains(random_number)){
                    random_number = random.nextInt(9000) + 1000;
                }
                random_ids.add(random_number);

                ChessGame chess_game = new ChessGame();

                game.createGame(random_number, "", "", game_name, chess_game);
                CreateGameResponse response = new CreateGameResponse(String.valueOf(random_number));
                response.setStatus(200);
                return response;





        }
        else if(game_name == null | current_token == null){
            Err error = new Err(400);
            return new CreateGameResponse(error);
        }
        else{
            Err error = new Err(401);
            return new CreateGameResponse(error);
        }

    }

}
