package services;


import dataAccess.*;
import model.*;

import java.util.Random;
import java.util.Vector;

import chess.*;
import server.requests.CreateGameRequest;
import server.responses.CreateGameResponse;


public class CreateGameService {

    private final CreateGameRequest request;

    private UserDAO user = new MemoryUserDAO();

    private AuthDAO auth = new MemoryAuthDAO();

    private GameDAO game = new MemoryGameDAO();

    private Vector<Integer> randomIds = new Vector<Integer>();

    public CreateGameService(CreateGameRequest req){
        this.request = req;

    }

    public CreateGameResponse createGame(String gameName, String currentToken){
        GameData gameData = game.getGame(gameName);
        AuthData userData = auth.getCurrentToken(currentToken);

        if (gameData == null & userData != null){
                Random random = new Random();
                int randomNumber = random.nextInt(9000) + 1000;
                while (randomIds.contains(randomNumber)){
                    randomNumber = random.nextInt(9000) + 1000;
                }
                randomIds.add(randomNumber);

                ChessGame chessGame = new ChessGame();

                game.createGame(randomNumber, "", "", gameName, chessGame);
                CreateGameResponse response = new CreateGameResponse(String.valueOf(randomNumber));
                response.setStatus(200);
                return response;





        }
        else if(gameName == null | currentToken == null){
            Err error = new Err(400);
            return new CreateGameResponse(error);
        }
        else{
            Err error = new Err(401);
            return new CreateGameResponse(error);
        }

    }

}
