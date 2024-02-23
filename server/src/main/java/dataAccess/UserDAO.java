package dataAccess;

import model.UserData;

import java.util.Vector;

public interface UserDAO {

    UserData getUser(String username);

    void createUser(String username, String password, String email, String authToken);


    void updateUser(UserData old_user, UserData new_user);

    void deleteUser() throws DataAccessException;


}
