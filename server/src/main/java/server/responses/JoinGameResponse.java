package server.responses;

import services.Err;

public class JoinGameResponse {

    private String message;

    private transient int status;


    public JoinGameResponse(Err error){
        this.message = error.getError().message();
        this.status = error.getError().status();
    }

    public JoinGameResponse(){

    }

    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status){
        this.status = status;
    }
}
