package responses;

public class ClearResponse {

    private String message;

    private transient int status;


    public ClearResponse(Err error){
        this.message = error.getError().message();
        this.status = error.getError().status();
    }

    public ClearResponse(){

    }

    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status){
        this.status = status;
    }
}
