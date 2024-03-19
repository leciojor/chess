package client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;

import com.google.gson.Gson;
import model.GameData;
import server.requests.*;
import server.responses.*;
import server.responses.RegisterResponse;



public class ClientCommunicator {

    private InputStream responseBody;

    private BufferedReader reader = null;

    private static String current_auth_token;

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
        StringBuilder response_builder = readingInputStream(responseBody);

        ErrorResponse response = gson.fromJson(response_builder.toString(), ErrorResponse.class);
        String error_message = response.getMessage();

        System.out.println();
        System.out.print(error_message);
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
        StringBuilder response_builder = readingInputStream(responseBody);


        if (Objects.equals(endpointType, "register")){
            RegisterResponse response = gson.fromJson(response_builder.toString(), RegisterResponse.class);
            System.out.println("Username: " + response.getUsername() );
            System.out.println("AuthToken: " + response.getAuthToken() );
            ClientCommunicator.current_auth_token = response.getAuthToken();
        }
        else if (Objects.equals(endpointType, "login")){
            LoginResponse response = gson.fromJson(response_builder.toString(), LoginResponse.class);
            System.out.println("Username: " + response.getUsername() );
            System.out.println("AuthToken: " + response.getAuthToken() );
            ClientCommunicator.current_auth_token = response.getAuthToken();
        }
        else if (Objects.equals(endpointType, "create")){
            CreateGameResponse response = gson.fromJson(response_builder.toString(), CreateGameResponse.class);
            System.out.println("GameID: " + response.getGameID() );

        }


    }

    public ClientCommunicator(URL url) throws IOException {
        this.url = url;
        this.connection = (HttpURLConnection) url.openConnection();
    }


    public void post(String input, String endpointType) throws IOException {

        setConfigs("POST", true);
        this.connection.addRequestProperty("Authorization", ClientCommunicator.current_auth_token);
        this.connection.connect();

        String[] inputsArray = input.split(" ");

        try(OutputStream requestBody = this.connection.getOutputStream();) {
            //I should have made the register classes with an inheritance organization for a cleaner code here
            serializationPost(requestBody, inputsArray, endpointType);
        }

        if (this.connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            System.out.println();
            deserializationPost(endpointType);
            ServerFacade.returned_error = false;
        }
        else{
            printErrorMessage();
            System.out.println();
            ServerFacade.returned_error = true;
        }

        this.connection.disconnect();

    }

    public void delete() throws IOException{
        setConfigs("DELETE", false);
        this.connection.addRequestProperty("Authorization",  ClientCommunicator.current_auth_token);
        this.connection.connect();

        if (this.connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            //still to add logic for clear endpoint
            System.out.println();
            System.out.println("Successfully Logged Out");
            ServerFacade.returned_error = false;

        }
        else {
            printErrorMessage();
            System.out.println();
            ServerFacade.returned_error = true;
        }

        this.connection.disconnect();

    }

    public void get() throws IOException{
        setConfigs("GET", false);
        this.connection.addRequestProperty("Authorization",  ClientCommunicator.current_auth_token);
        this.connection.connect();
        int i = 0;

        if (this.connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            responseBody = this.connection.getInputStream();
            StringBuilder response_builder = readingInputStream(responseBody);

            ListGamesResponse response = gson.fromJson(response_builder.toString(), ListGamesResponse.class);
            HashSet<GameData> games = response.getGames();
            System.out.println();
            System.out.println("Games List: ");

            for (GameData game : games){
                System.out.println();
                System.out.println(i + "." + " " +game.gameName() + "\n" + "White Username: " +  game.whiteUsername() + "\n" + "Black Username: " +  game.blackUsername());
                System.out.println();

                i++;
            }
            ServerFacade.returned_error = false;

        }
        else {
            printErrorMessage();
            System.out.println();
            ServerFacade.returned_error = true;;
        }

        this.connection.disconnect();

    }

    public void put(String input) throws IOException{
        setConfigs("PUT", true);
        this.connection.addRequestProperty("Authorization",  ClientCommunicator.current_auth_token);
        this.connection.connect();

        String[] inputsArray = input.split(" ");

        try(OutputStream requestBody = connection.getOutputStream()) {
            if (inputsArray.length == 2){
                JoinGameRequest request = new JoinGameRequest(inputsArray[0], inputsArray[1]);
                requestBody.write(gson.toJson(request).getBytes());
            }
            else{
                JoinGameRequest request = new JoinGameRequest(inputsArray[0], "OBSERVER");
                requestBody.write(gson.toJson(request).getBytes());
            }

        }

        if (this.connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            printErrorMessage();
            System.out.println();
            ServerFacade.returned_error = true;;
        }
        else{
            ServerFacade.returned_error = false;
            System.out.println();
            System.out.println("Successfully Joined Game");
        }


        this.connection.disconnect();

    }

}
