package services;


import dataAccess.*;
import model.*;
import server.*;

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
        GameData gameData = game.getGame(game_name);
        AuthData userData = auth.getCurrentToken(current_token);

        if (gameData == null & userData != null){
                Random random = new Random();
                int randomNumber = random.nextInt(9000) + 1000;
                while (random_ids.contains(randomNumber)){
                    randomNumber = random.nextInt(9000) + 1000;
                }
                random_ids.add(randomNumber);

                ChessGame chessGame = new ChessGame();

                game.createGame(randomNumber, "", "", game_name, chessGame);
                CreateGameResponse response = new CreateGameResponse(String.valueOf(randomNumber));
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
