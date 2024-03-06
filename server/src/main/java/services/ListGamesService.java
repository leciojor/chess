package services;

import dataAccess.*;
import model.AuthData;
import server.requests.ListGamesRequest;
import server.responses.ListGamesResponse;

import java.sql.SQLException;

public class ListGamesService {

    private ListGamesRequest request;

    private UserDAO user = new SQLUserDAO();

    private AuthDAO auth = new SQLAuthDAO();

    private GameDAO game = new SQLGameDAO();

    public ListGamesService(ListGamesRequest request){
        this.request = request;
    }

    public ListGamesResponse listGames(String currentToken) throws SQLException, DataAccessException {
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
