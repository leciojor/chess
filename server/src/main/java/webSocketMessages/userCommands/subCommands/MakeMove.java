package webSocketMessages.userCommands.subCommands;

import chess.ChessMove;

public class MakeMove {

    private int gameID;

    private ChessMove move;

    public MakeMove(int gameID, ChessMove move){
        this.gameID = gameID;
        this.move = move;
    }
}
