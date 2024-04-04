package webSocketMessages.serverMessages.subMessages;

import chess.ChessGame;

public class LoadGame {

    private ChessGame game;

    public LoadGame(ChessGame game){
        this.game = game;
    }
}
