package server;

public class JoinGameRequest {

    private String playerColor;

    private String gameID;

    public JoinGameRequest(String playerColor, String gameID){
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
