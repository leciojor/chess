package webSocketMessages.userCommands.subCommands;

import chess.ChessMove;
import webSocketMessages.userCommands.UserGameCommand;

public class MakeMove extends UserGameCommand {

    private int gameID;

    private ChessMove move;

    public MakeMove(int gameID, ChessMove move, String authToken){
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
    }


    public int getGameID(){
        return gameID;
    }

    public ChessMove getMove(){
        return move;
    }
}
