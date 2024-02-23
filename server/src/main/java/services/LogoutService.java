package services;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import server.LogoutRequest;
import server.LogoutResponse;
import services.Err;

import java.util.Objects;

public class LogoutService {

    private LogoutRequest request;

    private UserDAO user = new MemoryUserDAO();

    private AuthDAO auth = new MemoryAuthDAO();



    public LogoutService(LogoutRequest request){
        this.request = request;
    }

    public LogoutResponse logout(String current_token){
        AuthData user_data = auth.getCurrentToken();

        if (user_data != null){
            if (Objects.equals(user_data.authToken(), current_token)){
                auth.deleteAuth();
                LogoutResponse response = new LogoutResponse();
                response.setStatus(200);
                return response;
            }
            Err error = new Err(401);
            return new LogoutResponse(error);
        }
        else{
            Err error = new Err(500);
            return new LogoutResponse(error);
        }



    }

}
