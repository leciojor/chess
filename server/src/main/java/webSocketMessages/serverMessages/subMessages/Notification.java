package webSocketMessages.serverMessages.subMessages;

import webSocketMessages.serverMessages.ServerMessage;

public class Notification extends ServerMessage {

    private String message;


    public Notification(String message, ServerMessageType type){
        super(type);
        this.message = message;
    }
}
