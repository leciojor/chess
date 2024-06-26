package client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;

import com.google.gson.Gson;
import model.GameData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.*;


public class ClientCommunicator {

    public static String currentAuthToken;

    private InputStream responseBody;

    private BufferedReader reader = null;

    private Gson gson = new Gson();

    private URL url;

    private HttpURLConnection connection;

    private void setConfigs(String method, boolean doOutput) throws ProtocolException {
        connection.setReadTimeout(5000);
        connection.setRequestMethod(method);
        connection.setDoOutput(doOutput);
    }

    private StringBuilder readingInputStream(InputStream responseBody) throws IOException {
        reader = new BufferedReader(new InputStreamReader(responseBody));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        return response;
    }

    private void printErrorMessage() throws IOException {
        responseBody = connection.getErrorStream();
        StringBuilder responseBuilder = readingInputStream(responseBody);

        ErrorResponse response = gson.fromJson(responseBuilder.toString(), ErrorResponse.class);
        String errorMessage = response.getMessage();
        System.out.println();
        System.out.print(errorMessage.toUpperCase());
    }

    private void serializationPost(OutputStream requestBody, String[] inputsArray, String endpointType) throws IOException {
        if (Objects.equals(endpointType, "register")){
            RegisterRequest request = new RegisterRequest(inputsArray[0], inputsArray[1], inputsArray[2]);
            requestBody.write(gson.toJson(request).getBytes());
        }
        else if (Objects.equals(endpointType, "login")){
            LoginRequest request = new LoginRequest(inputsArray[0], inputsArray[1]);
            requestBody.write(gson.toJson(request).getBytes());
        }
        else if (Objects.equals(endpointType, "create")){
            CreateGameRequest request = new CreateGameRequest(inputsArray[0]);
            requestBody.write(gson.toJson(request).getBytes());
        }
    }

    private void deserializationPost(String endpointType) throws IOException {
        responseBody = connection.getInputStream();
        StringBuilder responseBuilder = readingInputStream(responseBody);


        if (Objects.equals(endpointType, "register")){
            RegisterResponse response = gson.fromJson(responseBuilder.toString(), RegisterResponse.class);
            System.out.println("Username: " + response.getUsername() );
            System.out.println("AuthToken: " + response.getAuthToken() );
            ClientCommunicator.currentAuthToken = response.getAuthToken();
        }
        else if (Objects.equals(endpointType, "login")){
            LoginResponse response = gson.fromJson(responseBuilder.toString(), LoginResponse.class);
            System.out.println("Username: " + response.getUsername() );
            System.out.println("AuthToken: " + response.getAuthToken() );
            ClientCommunicator.currentAuthToken = response.getAuthToken();
        }
        else if (Objects.equals(endpointType, "create")){
            CreateGameResponse response = gson.fromJson(responseBuilder.toString(), CreateGameResponse.class);
            System.out.println("GameID: " + response.getGameID() );

        }


    }

    public ClientCommunicator(URL url) throws IOException {
        this.url = url;
        this.connection = (HttpURLConnection) url.openConnection();
    }


    public void post(String input, String endpointType) throws IOException {

        setConfigs("POST", true);
        this.connection.addRequestProperty("Authorization", ClientCommunicator.currentAuthToken);
        this.connection.connect();

        String[] inputsArray = input.split(" ");

        try(OutputStream requestBody = this.connection.getOutputStream();) {
            //I should have made the register classes with an inheritance organization for a cleaner code here
            serializationPost(requestBody, inputsArray, endpointType);
        }

        if (this.connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            System.out.println();
            deserializationPost(endpointType);
            ServerFacade.returnedError = false;
        }
        else{
            printErrorMessage();
            System.out.println();
            ServerFacade.returnedError = true;
        }

        this.connection.disconnect();

    }

    public void delete() throws IOException{
        setConfigs("DELETE", false);
        this.connection.addRequestProperty("Authorization",  ClientCommunicator.currentAuthToken);
        this.connection.connect();

        if (this.connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            //still to add logic for clear endpoint
            System.out.println();
            System.out.println("Successfully Logged Out");
            ServerFacade.returnedError = false;

        }
        else {
            printErrorMessage();
            System.out.println();
            ServerFacade.returnedError = true;
        }

        this.connection.disconnect();

    }

    public void get() throws IOException{
        setConfigs("GET", false);
        this.connection.addRequestProperty("Authorization",  ClientCommunicator.currentAuthToken);
        this.connection.connect();
        int i = 0;

        if (this.connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            responseBody = this.connection.getInputStream();
            StringBuilder responseBuilder = readingInputStream(responseBody);

            ListGamesResponse response = gson.fromJson(responseBuilder.toString(), ListGamesResponse.class);
            HashSet<GameData> games = response.getGames();
            System.out.println();
            System.out.println("Games List: ");

            for (GameData game : games){
                System.out.println();

                if (!game.game().getIsOver()){
                    System.out.println(i + "." + " " +game.gameName() + "\n" + "White Username: " + game.whiteUsername() + "\n" + "Black Username: " +  game.blackUsername() + "\n" + "Game ID: " +  game.gameID());
                }
                else{
                    System.out.println(i + "." + " " +game.gameName() + "\n" + "GAME IS OVER");
                }

                System.out.println();

                i++;
            }
            ServerFacade.returnedError = false;

        }
        else {
            printErrorMessage();
            System.out.println();
            ServerFacade.returnedError = true;;
        }

        this.connection.disconnect();

    }

    public void put(String gameID, String perspective) throws IOException{
        setConfigs("PUT", true);
        this.connection.addRequestProperty("Authorization",  ClientCommunicator.currentAuthToken);
        this.connection.connect();

        try(OutputStream requestBody = connection.getOutputStream()) {
            JoinGameRequest request = new JoinGameRequest(gameID, perspective);
            requestBody.write(gson.toJson(request).getBytes());
        }

        if (this.connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            printErrorMessage();
            System.out.println();
            ServerFacade.returnedError = true;;
        }
        else{
            ServerFacade.returnedError = false;
            System.out.println();
        }


        this.connection.disconnect();

    }

}
