package server;

import model.GameData;
import services.Err;

import java.util.Vector;

public class ListGamesResponse {

    private String message;

    private transient int status;

    private Vector<GameData> game_list;


    public ListGamesResponse(Err error){
        this.message = error.getError().message();
        this.status = error.getError().status();
    }

    public ListGamesResponse(Vector<GameData> game_list){
        this.game_list = game_list;
    }

    public int getStatus(){
        return this.status;
    }

    public void setStatus(int status_){
        this.status = status_;
    }

}
