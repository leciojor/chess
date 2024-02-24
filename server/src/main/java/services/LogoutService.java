package services;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import model.AuthData;
import server.requests.LogoutRequest;
import server.responses.LogoutResponse;

public class LogoutService {

    private LogoutRequest request;

    private UserDAO user = new MemoryUserDAO();

    private AuthDAO auth = new MemoryAuthDAO();



    public LogoutService(LogoutRequest request){
        this.request = request;
    }

    public LogoutResponse logout(String currentToken){

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
