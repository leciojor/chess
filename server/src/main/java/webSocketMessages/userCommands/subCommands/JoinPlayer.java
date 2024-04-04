package webSocketMessages.userCommands.subCommands;

import chess.ChessGame.TeamColor;
import webSocketMessages.userCommands.UserGameCommand;


public class JoinPlayer extends UserGameCommand {

    private int gameID;

    private TeamColor playerColor;

    public JoinPlayer(int gameID, TeamColor playerColor, String authToken){
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public int getGameID(){
        return gameID;
    }

    public TeamColor getPlayerColor(){
        return playerColor;
    }
}
