package webSocketMessages.serverMessages.subMessages;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public class LoadGame extends ServerMessage {

    private int gameID;

    public LoadGame(int gameID, ServerMessageType type){
        super(type);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
