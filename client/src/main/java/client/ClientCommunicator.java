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



public class ClientCommunicator {

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
        System.out.println();
        System.out.print(responseBody);
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

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            printErrorMessage();
        }

        connection.disconnect();

    }

    public void delete(String path) throws IOException{

    }

    public void get(String path) throws IOException{

    }

    public void put(String path) throws IOException{

    }

}
