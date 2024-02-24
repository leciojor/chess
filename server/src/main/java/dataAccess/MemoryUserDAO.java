package dataAccess;

import java.util.Objects;
import java.util.Vector;

import model.UserData;

public class MemoryUserDAO implements UserDAO{

    private static Vector<UserData> userList = new Vector<UserData>();

    @Override
    public UserData getUser(String username){
        if (userList != null){
            for (int i = 0; i < userList.size(); i++){
                if (Objects.equals(userList.get(i).username(), username)){
                    return userList.get(i);
                }
            }
        }

        return null;
    }


    @Override
    public void createUser(String username, String password, String email, String authToken) {
        UserData user = new UserData(username, password, email, authToken);
        userList.add(user);
    }


    @Override
    public void updateUser(UserData oldUser, UserData newUser) {
        userList.remove(oldUser);
        userList.add(newUser);
    }

    @Override
    public void deleteUser() throws DataAccessException{
        userList = new Vector<UserData>();
    }
}
