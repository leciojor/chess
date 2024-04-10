package responses;

public class CreateGameResponse {


    private String gameID;
    private String message;
    private transient int status;




    public CreateGameResponse(String gameID){
        this.gameID = gameID;

    }

    public CreateGameResponse(Err error){
        this.message = error.getError().message();
        this.status = error.getError().status();
    }



    public int getStatus(){
        return this.status;
    }

    public String getGameID(){
        return this.gameID;
    }

    public void setStatus(int status){
        this.status = status;
    }


}
