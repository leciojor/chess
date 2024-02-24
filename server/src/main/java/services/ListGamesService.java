package services;

import dataAccess.*;
import model.AuthData;
import server.*;

import java.util.Objects;

public class ListGamesService {

    private ListGamesRequest request;

    private UserDAO user = new MemoryUserDAO();

    private AuthDAO auth = new MemoryAuthDAO();

    private GameDAO game = new MemoryGameDAO();

    public ListGamesService(ListGamesRequest request){
        this.request = request;
    }

    public ListGamesResponse listGames(String currentToken){
        AuthData userData = auth.getCurrentToken(currentToken);

        if (userData != null){
                ListGamesResponse response = new ListGamesResponse(game.getListGames());
                response.setStatus(200);
                return response;

        }
        else{
            Err error = new Err(401);
            return new ListGamesResponse(error);
        }

    }

}
