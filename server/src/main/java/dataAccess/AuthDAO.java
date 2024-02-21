package dataAccess;

import model.AuthData;

import java.util.Vector;

public interface AuthDAO {


    String getCurrentToken();

    void createAuth(String username);

    void readAuth();

    void updateAuth();

    void deleteAuth();
}
