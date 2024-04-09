package webSocketMessages.userCommands.subCommands;

import webSocketMessages.userCommands.UserGameCommand;

public class JoinObserver extends UserGameCommand {
    private int gameID;

    public JoinObserver(int gameID, String authToken){
        super(authToken);

        this.gameID = gameID;
    }

}
