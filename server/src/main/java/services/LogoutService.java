package services;

import dataAccess.*;
import model.AuthData;
import requests.LogoutRequest;
import responses.Err;
import responses.LogoutResponse;

import java.sql.SQLException;

public class LogoutService {

    private LogoutRequest request;

    private UserDAO user = new SQLUserDAO();

    private AuthDAO auth = new SQLAuthDAO();



    public LogoutService(LogoutRequest request){
        this.request = request;
    }

    public LogoutResponse logout(String currentToken) throws SQLException, DataAccessException {

        AuthData userData = auth.getCurrentToken(currentToken);

        if (userData != null){
                auth.deleteAuth(userData);
                LogoutResponse response = new LogoutResponse();
                response.setStatus(200);
                return response;

        }
        else{
            Err error = new Err(401);
            return new LogoutResponse(error);
        }



    }

}
