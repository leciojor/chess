package dataAccess;

import model.UserData;

import dataAccess.DatabaseManager;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{


    @Override
    public UserData getUser(String username) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT id, username, password, email, authtoken FROM User WHERE username=?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (!rs.next()){
                        return null;
                    }
                    while (rs.next()) {
                        var name = rs.getString("username");
                        var password = rs.getString("password");
                        var email = rs.getString("email");
                        var authToken = rs.getString("authtoken");

                        return new UserData(name, password, email, authToken);
                    }
                }
            }
        }

    }

    @Override
    public void createUser(String username, String password, String email, String authToken) throws DataAccessException, SQLException {

    }

    @Override
    public void updateUser(UserData oldUser, UserData newUser) throws DataAccessException, SQLException{

    }

    @Override
    public void deleteUser() throws DataAccessException throws DataAccessException{

    }
}
