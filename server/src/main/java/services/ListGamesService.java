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

    public ListGamesResponse listGames(String current_token){
        AuthData user_data = auth.getCurrentToken();

        if (user_data != null){
            if (Objects.equals(user_data.authToken(), current_token)){
                ListGamesResponse response = new ListGamesResponse(game.getListGames());
                response.setStatus(200);
                return response;
            }
            Err error = new Err(401);
            return new ListGamesResponse(error);
        }
        else{
            Err error = new Err(500);
            return new ListGamesResponse(error);
        }

    }

}
