package services;

import dataAccess.*;
import server.responses.ClearResponse;

import java.sql.SQLException;

public class ClearService {

    private UserDAO user = new SQLUserDAO();

    private AuthDAO auth = new SQLAuthDAO();

    private GameDAO game = new SQLGameDAO();

    public ClearService(){

    }

    public ClearResponse clear() throws SQLException {
        try{
            user.deleteUser();
            auth.deleteAuthList();
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
