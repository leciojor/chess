package dataAccess;

import model.UserData;

public class SQLUserDAO implements UserDAO{
    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email, String authToken) {

    }

    @Override
    public void updateUser(UserData oldUser, UserData newUser) {

    }

    @Override
    public void deleteUser() throws DataAccessException {

    }
}
