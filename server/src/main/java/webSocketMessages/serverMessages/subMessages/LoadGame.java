package webSocketMessages.serverMessages.subMessages;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public class LoadGame extends ServerMessage {

    private ChessGame game;

    public LoadGame(ChessGame game, ServerMessageType type){
        super(type);
        this.game = game;
    }

    public Object getGame() {
        return game;
    }
}
