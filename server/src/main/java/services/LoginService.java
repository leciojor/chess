package services;

import dataAccess.*;
import model.UserData;
import server.LoginRequest;
import server.LoginResponse;

import java.util.Objects;

public class LoginService {
    private final LoginRequest request;

    private UserDAO user = new MemoryUserDAO();

    private AuthDAO auth = new MemoryAuthDAO();

    public LoginService(LoginRequest req){
        this.request = req;

    }


    public LoginResponse login(String username, String password) {
        UserData user_data = user.getUser(username);


        if (user_data != null){
            if (Objects.equals(user_data.password(), password)){
                auth.createAuth(username);
                String current_token = auth.getCurrentToken(user_data.authToken()).authToken();
                LoginResponse response = new LoginResponse(username, current_token);
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
