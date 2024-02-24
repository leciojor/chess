package dataAccess;

import model.AuthData;

import java.util.Vector;

public interface AuthDAO {


    AuthData getCurrentToken(String token);

    Vector<AuthData> getCurrentAuths();

    void createAuth(String username);



    void deleteAuth(AuthData token);

    void deleteAuthList();
}
