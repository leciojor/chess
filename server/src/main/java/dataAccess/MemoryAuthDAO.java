package dataAccess;

import model.AuthData;

import java.util.UUID;
import java.util.Vector;

public class MemoryAuthDAO implements AuthDAO{

    private static AuthData current_auth;

    @Override
    public String getCurrentToken() {
        return current_auth.authToken();
    }

    @Override
    public void createAuth(String username) {
        String random_token = UUID.randomUUID().toString();
        current_auth = new AuthData(random_token, username);
    }

    @Override
    public void readAuth() {

    }

    @Override
    public void updateAuth() {

    }

    @Override
    public void deleteAuth() {

    }
}
