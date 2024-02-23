package dataAccess;

import model.AuthData;

import java.util.Vector;

public interface AuthDAO {


    AuthData getCurrentToken();

    void createAuth(String username);

    void updateAuth();

    void deleteAuth();
}
