package requests;

public class JoinGameRequest {

    private String playerColor;

    private String gameID;

    public JoinGameRequest(String gameID, String playerColor){
        this.playerColor = playerColor;
        this.gameID = gameID;

    }

    public String getColor(){
        return this.playerColor;
    }

    public String getid(){
        return this.gameID;
    }


}
