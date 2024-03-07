package dataAccess;

import model.*;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import java.util.Vector;

public class MemoryAuthDAO implements AuthDAO{

    private static Vector<AuthData> currentAuths = new Vector<AuthData>();

    @Override
    public AuthData getCurrentToken(String token) {
        if (currentAuths != null){
            for (int i = 0; i < currentAuths.size(); i++){
                if (Objects.equals(currentAuths.get(i).authToken(), token)){
                    return currentAuths.get(i);
                }
            }
        }

        return null;
    }

    @Override
    public String getTokenValue(String username) throws DataAccessException, SQLException {
        return null;
    }

    @Override
    public void createAuth(String username) throws SQLException, DataAccessException {
        UserDAO user = new MemoryUserDAO();
        //may eventually generate the same token from before
        String randomToken = UUID.randomUUID().toString();
        UserData data = user.getUser(username);
        if (data != null){
            UserData tempUser = new UserData(username, data.password(), data.email(), randomToken);
            user.updateUser(data, tempUser);
        }

        AuthData currentAuth = new AuthData(randomToken, username);
        currentAuths.add(currentAuth);

    }



    public Vector<AuthData> getCurrentAuths(){
        return currentAuths;
    }

    @Override
    public void deleteAuth(AuthData token) {
        currentAuths.remove(token);
    }

    @Override
    public void deleteAuthList() {
        currentAuths = new Vector<AuthData>();
    }
}
