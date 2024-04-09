package webSocketMessages.serverMessages.subMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class ErrorMessage extends ServerMessage {

    private String errorMessage;

    public ErrorMessage(String errorMessage, ServerMessageType type){
        super(type);
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
