package dataAccess;

import model.UserData;

import java.sql.SQLException;
import java.util.Vector;

public interface UserDAO {

    UserData getUser(String username) throws DataAccessException, SQLException;

    void createUser(String username, String password, String email, String authToken) throws DataAccessException, SQLException;


    void updateUser(UserData oldUser, UserData newUser) throws DataAccessException, SQLException;

    void deleteUser() throws DataAccessException, SQLException;


}
