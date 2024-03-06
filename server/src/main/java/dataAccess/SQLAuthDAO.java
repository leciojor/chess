package dataAccess;

import model.AuthData;

import java.util.Vector;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public AuthData getCurrentToken(String token) {
        return null;
    }

    @Override
    public Vector<AuthData> getCurrentAuths() {
        return null;
    }

    @Override
    public void createAuth(String username) {

    }

    @Override
    public void deleteAuth(AuthData token) {

    }

    @Override
    public void deleteAuthList() {

    }
}
