package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.Vector;

public interface AuthDAO {


    AuthData getCurrentToken(String token);

    Vector<AuthData> getCurrentAuths();

    void createAuth(String username) throws SQLException, DataAccessException;



    void deleteAuth(AuthData token);

    void deleteAuthList();
}
