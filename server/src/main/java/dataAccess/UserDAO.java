package dataAccess;

import model.UserData;

import java.util.Vector;

public interface UserDAO {

    UserData getUser(String username);

    void createUser(String username, String password, String email);

    void readUser();

    void updateUser();

    void deleteUser();


}
