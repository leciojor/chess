package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.Vector;

public interface AuthDAO {


    AuthData getCurrentToken(String token) throws DataAccessException, SQLException;

    Vector<AuthData> getCurrentAuths() throws DataAccessException, SQLException;

    void createAuth(String username) throws SQLException, DataAccessException;



    void deleteAuth(AuthData token) throws DataAccessException, SQLException;

    void deleteAuthList() throws DataAccessException, SQLException;
}
