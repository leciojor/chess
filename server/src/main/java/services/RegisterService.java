package services;

import model.UserData;
import server.RegisterRequest;

import dataAccess.*;
import server.RegisterResponse;

import java.util.Objects;

public class RegisterService {

    private final RegisterRequest request;

    public RegisterService(RegisterRequest req){
        this.request = req;

    }

    public RegisterRequest getRequest(){
        return this.request;

    }

    public RegisterResponse register(String username, String password, String email)  {
        UserDAO user = new MemoryUserDAO();
        AuthDAO auth = new MemoryAuthDAO();
        auth.createAuth(username);
        String current_token = auth.getCurrentToken();
        UserData user_data = user.getUser(username);


        if (user_data == null){
            if(Objects.equals(user_data.password(), "") | Objects.equals(user_data.username(), "") | Objects.equals(user_data.email(), "")){
                Err error = new Err(400);
                return new RegisterResponse(error);
            }
            user.createUser(username, password, email);
            RegisterResponse response = new RegisterResponse(username, current_token);
            response.setStatus(200);
            return response;
        }

        else if (user_data != null){
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
