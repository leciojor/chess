package services;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import server.LoginRequest;
import server.LoginResponse;

import java.util.Objects;
import java.util.Vector;

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
                Vector<AuthData> auths_list = auth.getCurrent_auths();
                String current_token = auth.getCurrentToken(auths_list.get(auths_list.size() - 1 ).authToken()).authToken();
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
