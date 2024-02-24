package dataAccess;

import model.AuthData;

import java.util.Vector;

public interface AuthDAO {


    AuthData getCurrentToken(String token);

    Vector<AuthData> getCurrent_auths();

    void createAuth(String username);

    void updateAuth();

    void deleteAuth();
}
