package dataAccess;

import model.*;

import java.util.UUID;
import java.util.Vector;

public class MemoryAuthDAO implements AuthDAO{

    private static AuthData current_auth;

    @Override
    public AuthData getCurrentToken() {
        return current_auth;
    }

    @Override
    public void createAuth(String username) {
        UserDAO user = new MemoryUserDAO();
        String random_token = UUID.randomUUID().toString();
        UserData data = user.getUser(username);
        if (data != null){
            UserData temp_user = new UserData(username, data.password(), data.email(), random_token);
            user.updateUser(data, temp_user);
        }

        current_auth = new AuthData(random_token, username);

    }


    @Override
    public void updateAuth() {

    }

    @Override
    public void deleteAuth() {
        current_auth = null;
    }
}
