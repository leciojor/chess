package dataAccess;

import model.UserData;

import java.util.Vector;

public interface UserDAO {

    Vector<UserData> getUserData();

    Boolean userAlreadyExists(String username, String password, String email);

    void createUser(String username, String password, String email);

    void readUser();

    void updateUser();

    void deleteUser();


}
