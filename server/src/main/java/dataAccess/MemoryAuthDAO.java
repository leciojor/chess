package dataAccess;

import model.*;

import java.util.Objects;
import java.util.UUID;
import java.util.Vector;

public class MemoryAuthDAO implements AuthDAO{

    private static Vector<AuthData> current_auths = new Vector<AuthData>();

    @Override
    public AuthData getCurrentToken(String token) {
        if (current_auths != null){
            for (int i = 0; i < current_auths.size(); i++){
                if (Objects.equals(current_auths.get(i).authToken(), token)){
                    return current_auths.get(i);
                }
            }
        }

        return null;
    }

    @Override
    public void createAuth(String username) {
        UserDAO user = new MemoryUserDAO();
        //may eventually generate the same token from before
        String random_token = UUID.randomUUID().toString();
        UserData data = user.getUser(username);
        if (data != null){
            UserData temp_user = new UserData(username, data.password(), data.email(), random_token);
            user.updateUser(data, temp_user);
        }

        AuthData current_auth = new AuthData(random_token, username);
        current_auths.add(current_auth);

    }


    @Override
    public void updateAuth() {

    }

    public Vector<AuthData> getCurrent_auths(){
        return current_auths;
    }

    @Override
    public void deleteAuth(AuthData token) {
        current_auths.remove(token);
    }

    @Override
    public void deleteAuthList() {
        current_auths = new Vector<AuthData>();
    }
}
