package responses;

public class RegisterResponse {

    private String username;

    private String authToken;
    private String message;
    private transient int status;



    public RegisterResponse(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }

    public RegisterResponse(Err error){
        this.message = error.getError().message();
        this.status = error.getError().status();
    }


    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public String getUsername(){
        return username;
    }

    public String getAuthToken(){
        return this.authToken;
    }





}
