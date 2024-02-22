package services;

import dataAccess.*;
import model.UserData;
import server.LoginRequest;
import server.LoginResponse;
import server.RegisterRequest;
import server.RegisterResponse;

import java.util.Objects;

public class LoginService {
    private final LoginRequest request;

    public LoginService(LoginRequest req){
        this.request = req;

    }

    public LoginRequest getRequest(){
        return this.request;

    }

    public LoginResponse login(String username, String password) {
        UserDAO user = new MemoryUserDAO();
        AuthDAO auth = new MemoryAuthDAO();
        auth.createAuth(username);
        String current_token = auth.getCurrentToken();
        UserData user_data = user.getUser(username);

        if (user_data != null){
            if (Objects.equals(user_data.password(), password)){
                return new LoginResponse(username, current_token);
            }
            else{
                Err error = new Err(401);
                return new LoginResponse(error);
            }
        }
        else{
            Err error = new Err(500);
            return new LoginResponse(error);
        }

    }

}
