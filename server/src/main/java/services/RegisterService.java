package services;

import server.registerRequest;

public class RegisterService {

    private registerRequest request;

    void RegisterService(registerRequest req){
        this.request = req;

    }

    public registerRequest getRequest(){
        return this.request;

    }

    public void register(String username, String password, String email){


    }


}
