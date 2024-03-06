package services;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import server.requests.LoginRequest;
import server.responses.LoginResponse;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Vector;

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
