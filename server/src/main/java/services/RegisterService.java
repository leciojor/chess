package services;

import model.AuthData;
import model.UserData;
import server.RegisterRequest;

import dataAccess.*;
import server.RegisterResponse;

import java.util.Objects;
import java.util.Vector;

public class RegisterService {

    private final RegisterRequest request;

    private UserDAO user = new MemoryUserDAO();

    private AuthDAO auth = new MemoryAuthDAO();

    public RegisterService(RegisterRequest req){
        this.request = req;

    }


    public RegisterResponse register(String username, String password, String email)  {
        UserData userData = user.getUser(username);

        if (userData == null & username != null & password != null & email != null){
            auth.createAuth(username);
            Vector<AuthData> authsList = auth.getCurrentAuths();
            String currentToken = auth.getCurrentToken(authsList.get(authsList.size() - 1 ).authToken()).authToken();
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

        //return authtoken and username (will need to create the classes and objects from dataAccess and models)

        //DATAACCESS should think on WHAT YOU HIDE YOU CAN CHANGE
        //UserDao (interface) daa = new MemoryYserDao();

        //dao.createUser();
        //then we can just change it to UserDao (interface) daa = new SQLUserDao(); without any problems

        //WHAT YOU HIDE YOU CAN CHANGE


        //USE THE SAME INSTANCES IN DATAACCESS, OR THE TESTS WILL FAIL - use static variables (final pass new copied instances)
        //so, USE STATIC VARIABLES WHEN ACCESSING THE DATA WHILE THE DATABASE IS NOT DEVELOPED


    }


}
