package webSocketMessages.userCommands.subCommands;

import webSocketMessages.userCommands.UserGameCommand;

public class Leave extends UserGameCommand {

    private int gameID;

    public Leave(int gameID, String authToken){
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.LEAVE;
    }

    public int getGameID(){
        return gameID;
    }
}
