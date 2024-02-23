package services;

import dataAccess.*;
import server.ClearResponse;
import server.JoinGameResponse;

public class ClearService {

    private UserDAO user = new MemoryUserDAO();

    private AuthDAO auth = new MemoryAuthDAO();

    private GameDAO game = new MemoryGameDAO();

    public ClearService(){

    }

    public ClearResponse clear(){
        try{
            user.deleteUser();
            auth.deleteAuth();
            game.deleteGame();

            ClearResponse response = new ClearResponse();
            response.setStatus(200);
            return response;
        }
        catch (DataAccessException e){
            Err error = new Err(500);
            return new ClearResponse(error);
        }


    }

}
