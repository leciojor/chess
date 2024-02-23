package dataAccess;

import java.util.Objects;
import java.util.Vector;

import model.UserData;

public class MemoryUserDAO implements UserDAO{

    private static Vector<UserData> user_list = new Vector<UserData>();

    @Override
    public UserData getUser(String username){
        if (user_list != null){
            for (int i = 0; i < user_list.size(); i++){
                if (Objects.equals(user_list.get(i).username(), username)){
                    return user_list.get(i);
                }
            }
        }

        return null;
    }


    @Override
    public void createUser(String username, String password, String email, String authToken) {
        UserData user = new UserData(username, password, email, authToken);
        user_list.add(user);
    }


    @Override
    public void updateUser(UserData old_user, UserData new_user) {
        user_list.remove(old_user);
        user_list.add(new_user);
    }

    @Override
    public void deleteUser() throws DataAccessException{
        user_list.clear();
    }
}
