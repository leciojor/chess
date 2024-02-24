package server;

import services.Err;

public class LoginResponse {
    private String username;

    private String authToken;

    private String message;

    private transient int status;

    public LoginResponse(String username, String authToken){
        this.username = username;
        this.authToken = authToken;

    }

    public LoginResponse(Err error){
        this.message = error.getError().message();
        this.status = error.getError().status();
    }


    public String getUsername(){
        return username;
    }

    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status_){
        this.status = status_;
    }



}
