package services;

import dataAccess.*;
import server.LoginRequest;
import server.LoginResponse;
import server.RegisterRequest;
import server.RegisterResponse;

public class LoginService {
    private final LoginRequest request;

    public LoginService(LoginRequest req){
        this.request = req;

    }

    public LoginRequest getRequest(){
        return this.request;

    }

    public LoginResponse login(String username, String password) throws DataAccessException {
        UserDAO user = new MemoryUserDAO();
        AuthDAO auth = new MemoryAuthDAO();
        auth.createAuth(username);
        String current_token = auth.getCurrentToken();

        if (user.getUser(username) != null){
            return new LoginResponse(username, current_token);
        }
        else{
            throw new DataAccessException("There is no account registered with this username");
        }

    }

}
