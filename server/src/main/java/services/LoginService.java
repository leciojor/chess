package services;

import dataAccess.*;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import requests.LoginRequest;
import responses.Err;
import responses.LoginResponse;

import java.sql.SQLException;

public class LoginService {
    private final LoginRequest request;

    private UserDAO user = new SQLUserDAO();

    private AuthDAO auth = new SQLAuthDAO();

    public LoginService(LoginRequest req){
        this.request = req;

    }


    public LoginResponse login(String username, String password) throws SQLException, DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UserData userData = user.getUser(username);

        if (userData != null){
            if (encoder.matches(password, userData.password())){
                auth.createAuth(username);
                String currentToken = auth.getTokenValue(username);
                LoginResponse response = new LoginResponse(username, currentToken);
                response.setStatus(200);
                return response;
            }
            else{
                Err error = new Err(401);
                return new LoginResponse(error);
            }
        }
        else{
            Err error = new Err(401);
            return new LoginResponse(error);
        }

    }

}
