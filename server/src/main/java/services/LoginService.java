package services;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import server.requests.LoginRequest;
import server.responses.LoginResponse;

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
        UserData userData = user.getUser(username);


        if (userData != null){
            if (Objects.equals(userData.password(), password)){
                auth.createAuth(username);
                Vector<AuthData> authsList = auth.getCurrentAuths();
                String currentToken = auth.getCurrentToken(authsList.get(authsList.size() - 1 ).authToken()).authToken();
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
