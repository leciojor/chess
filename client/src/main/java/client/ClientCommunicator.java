package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Objects;

import com.google.gson.Gson;
import server.requests.*;
import server.responses.*;
import server.responses.RegisterResponse;


public class ClientCommunicator {

    private String current_auth_token;

    private Gson gson = new Gson();

    private URL url;

    private HttpURLConnection connection;

    private void setConfigs(String method, boolean doOutput) throws ProtocolException {
        connection.setReadTimeout(5000);
        connection.setRequestMethod(method);
        connection.setDoOutput(doOutput);
    }

    private void printErrorMessage(){
        InputStream responseBody = connection.getErrorStream();
        String error_message = gson.toJson(responseBody);

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
        InputStream responseBody = connection.getInputStream();


        if (Objects.equals(endpointType, "register")){
            RegisterResponse response = gson.fromJson(responseBody.toString(), RegisterResponse.class);
            System.out.println("Username: " + response.getUsername() );
            System.out.println("AuthToken: " + response.getAuthToken() );
            current_auth_token = response.getAuthToken();
        }
        else if (Objects.equals(endpointType, "login")){
            LoginResponse response = gson.fromJson(responseBody.toString(), LoginResponse.class);
            System.out.println("Username: " + response.getUsername() );
            System.out.println("AuthToken: " + response.getAuthToken() );
            current_auth_token = response.getAuthToken();
        }
        else if (Objects.equals(endpointType, "create")){
            CreateGameResponse response = gson.fromJson(responseBody.toString(), CreateGameResponse.class);
            System.out.println("GameID: " + response.getGameID() );
            System.out.println("AuthToken: " + connection.getHeaderField("Authorization"));
            current_auth_token = connection.getHeaderField("Authorization");
        }


    }

    public ClientCommunicator(URL url) throws IOException {
        this.url = url;
        this.connection = (HttpURLConnection) url.openConnection();
    }


    public void post(String input, String endpointType) throws IOException {

        setConfigs("POST", true);
        connection.connect();

        String[] inputsArray = input.split(" ");

        try(OutputStream requestBody = connection.getOutputStream();) {
            //I should have made the register classes with an inheritance organization for a cleaner code here
            serializationPost(requestBody, inputsArray, endpointType);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            System.out.println();
            deserializationPost(endpointType);
        }
        else{
            printErrorMessage();
        }

        connection.disconnect();

    }

    public void delete(String input) throws IOException{

    }

    public void get(String input) throws IOException{
        setConfigs("GET", false);
        connection.addRequestProperty("Authorization",  current_auth_token);
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary

            InputStream responseBody = connection.getInputStream();
            // print list of games based on assignment requirements (check)
        } else {
            printErrorMessage();
        }


    }

    public void put(String input) throws IOException{

    }

}
