package webSocketMessages.userCommands.subCommands;

import chess.ChessGame.TeamColor;


public class JoinPlayer {

    private int gameID;

    private TeamColor playerColor;

    public JoinPlayer(int gameID, TeamColor playerColor){
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
}
