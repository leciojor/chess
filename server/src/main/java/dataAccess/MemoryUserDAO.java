package dataAccess;

import java.util.Vector;

import model.UserData;

public class MemoryUserDAO implements UserDAO{

    private Vector<UserData> user_list = new Vector<UserData>();

    @Override
    public Vector<UserData> getUserData(){
        return user_list;
    }



    @Override
    public Boolean userAlreadyExists(String username, String password, String email) {
        UserData temp_user = new UserData(username, password, email);
        //may need to override equals method on user_data
        if (user_list.contains(temp_user)){
            return true;
        }
        return false;
    }


    @Override
    public void createUser(String username, String password, String email) {
        UserData user = new UserData(username, password, email);
        user_list.add(user);
    }

    @Override
    public void readUser() {

    }

    @Override
    public void updateUser() {

    }

    @Override
    public void deleteUser() {

    }
}
