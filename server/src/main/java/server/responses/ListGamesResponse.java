package server.responses;

import model.GameData;
import services.Err;

import java.util.HashSet;
import java.util.Vector;

public class ListGamesResponse {

    private String message;

    private transient int status;

    private HashSet<GameData> games;


    public ListGamesResponse(Err error){
        this.message = error.getError().message();
        this.status = error.getError().status();
    }

    public ListGamesResponse(HashSet<GameData> games){
        this.games = games;
    }

    public int getStatus(){
        return this.status;
    }

    public HashSet<GameData> getGames(){
        return this.games;
    }

    public void setStatus(int status){
        this.status = status;
    }

}
