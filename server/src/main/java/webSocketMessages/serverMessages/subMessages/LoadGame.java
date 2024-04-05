package webSocketMessages.serverMessages.subMessages;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public class LoadGame extends ServerMessage {

    private int game;

    public LoadGame(int game, ServerMessageType type){
        super(type);
        this.game = game;
    }

    public int getGame() {
        return game;
    }
}
