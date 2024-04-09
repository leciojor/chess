package services;

import model.UserData;
import requests.RegisterRequest;

import dataAccess.*;
import responses.RegisterResponse;

import java.sql.SQLException;
import java.util.Objects;

public class RegisterService {

    private final RegisterRequest request;

    private UserDAO user = new SQLUserDAO();

    private AuthDAO auth = new SQLAuthDAO();

    public RegisterService(RegisterRequest req){
        this.request = req;

    }


    public RegisterResponse register(String username, String password, String email) throws SQLException, DataAccessException {
        UserData userData = user.getUser(username);

        if (userData == null & username != null & password != null & email != null){
            auth.createAuth(username);
            String currentToken = auth.getTokenValue(username);
            user.createUser(username, password, email, currentToken);
            RegisterResponse response = new RegisterResponse(username, currentToken);
            response.setStatus(200);
            return response;
        }
        else if(Objects.equals(password, null) | Objects.equals(username, null) | Objects.equals(email, null)){
            Err error = new Err(400);
            return new RegisterResponse(error);
        }
        else if (userData != null){
            Err error = new Err(403);
            return new RegisterResponse(error);
        }
        else{
            Err error = new Err(500);
            return new RegisterResponse(error);
        }



    }


}
