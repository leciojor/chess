package services;

import server.registerRequest;

public class RegisterService {

    private registerRequest request;

    public RegisterService(registerRequest req){
        this.request = req;

    }

    public registerRequest getRequest(){
        return this.request;

    }

    public void register(String username, String password, String email){
        //check if user exists with UserDAO
        //If it doesnt, createone
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
