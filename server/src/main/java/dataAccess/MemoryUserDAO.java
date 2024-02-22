package dataAccess;

import java.util.Objects;
import java.util.Vector;

import model.UserData;

public class MemoryUserDAO implements UserDAO{

    private static Vector<UserData> user_list = new Vector<UserData>();

    @Override
    public UserData getUser(String username){
        for (int i = 0; i < user_list.size(); i++){
            if (Objects.equals(user_list.get(i).username(), username)){
                return user_list.get(i);
            }
        }
        return null;
    }


    @Override
    public void createUser(String username, String password, String email) {
        UserData user = new UserData(username, password, email);
        user_list.add(user);
    }


    @Override
    public void updateUser() {

    }

    @Override
    public void deleteUser() {

    }
}
