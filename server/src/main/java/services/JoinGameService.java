package services;


import dataAccess.*;
import model.AuthData;
import model.*;
import requests.JoinGameRequest;
import responses.Err;
import responses.JoinGameResponse;

import java.sql.SQLException;
import java.util.Objects;

public class JoinGameService {

    private JoinGameRequest request;

    private UserDAO user = new SQLUserDAO();

    private AuthDAO auth = new SQLAuthDAO();

    private GameDAO game = new SQLGameDAO();



    public JoinGameService(JoinGameRequest request){
        this.request = request;
    }

    public JoinGameResponse joinGame(String color, String gameid, String authtoken) throws SQLException, DataAccessException {
        AuthData userData = auth.getCurrentToken(authtoken);
        GameData gameData = game.getGameByID(gameid);

        if (gameData != null & userData != null){

                if (Objects.equals(color, "WHITE") || Objects.equals(color, "BLACK")){
                    if (Objects.equals(color, "WHITE") && Objects.equals(gameData.whiteUsername(), null)){
                        GameData newUserGameData = new GameData(Integer.parseInt(gameid), userData.username(),
                                gameData.blackUsername(), gameData.gameName(), gameData.game());
                        game.addUser(gameData, newUserGameData);

                        JoinGameResponse response = new JoinGameResponse();
                        response.setStatus(200);
                        return response;
                    }
                    else if (Objects.equals(color, "BLACK") && Objects.equals(gameData.blackUsername(), null)){
                        GameData newUserGameData = new GameData(Integer.parseInt(gameid), gameData.whiteUsername(),
                                userData.username(), gameData.gameName(), gameData.game());
                        game.addUser(gameData, newUserGameData);

                        JoinGameResponse response = new JoinGameResponse();
                        response.setStatus(200);
                        return response;
                    }
                    Err error = new Err(403);
                    return new JoinGameResponse(error);
                }
                else if (gameid == null || authtoken == null){
                    Err error = new Err(400);
                    return new JoinGameResponse(error);
                }
                else{
                    JoinGameResponse response = new JoinGameResponse();
                    response.setStatus(200);
                    return response;
                }


        }
        else if (gameData == null){
            Err error = new Err(400);
            return new JoinGameResponse(error);
        }

        Err error = new Err(401);
        return new JoinGameResponse(error);


    }

}
