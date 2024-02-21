package services;

import server.RegisterRequest;

import dataAccess.*;
import server.RegisterResponse;

public class RegisterService {

    private final RegisterRequest request;

    public RegisterService(RegisterRequest req){
        this.request = req;

    }

    public RegisterRequest getRequest(){
        return this.request;

    }

    public RegisterResponse register(String username, String password, String email){
        //needs to change logic so it is always the same instance (MAYBE INITIALIZE ON THE SERVER CLASS)
        UserDAO user = new MemoryUserDAO();
        AuthDAO auth = new MemoryAuthDAO();
        auth.createAuth(username);
        String current_token = auth.getCurrentToken();

        //check if user exists with UserDAO
        if (user.getUser(username) == null){
            return new RegisterResponse(username, current_token);
        }
        //If it doesnt, createone
        else{
            user.createUser(username, password, email);
            return new RegisterResponse(username, current_token);
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
