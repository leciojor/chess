package responses;

public class ErrorResponse {

    private String message;
    private transient int status;


    public ErrorResponse(Err error){
        this.message = error.getError().message();
        this.status = error.getError().status();
    }

    public String getMessage(){
        return this.message;
    }
}
