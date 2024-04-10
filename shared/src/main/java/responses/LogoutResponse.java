package responses;

public class LogoutResponse {

    private String message;

    private transient int status;


    public LogoutResponse(Err error){
        this.message = error.getError().message();
        this.status = error.getError().status();
    }

    public LogoutResponse(){

    }

    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status){
        this.status = status;
    }


}
