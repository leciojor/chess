package requests;

public class CreateGameRequest{

    private String gameName;

    public CreateGameRequest(String gameName){
        this.gameName = gameName;
    }

    public String getName(){
        return gameName;

    }

}
