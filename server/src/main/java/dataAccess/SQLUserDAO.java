package dataAccess;

import model.UserData;

import dataAccess.DatabaseManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{

    @Override
    public UserData getUser(String username) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT id, username, password, email, authtoken FROM User WHERE username=?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {

                    if (rs.next()) {
                        var name = rs.getString("username");
                        var password = rs.getString("password");
                        var email = rs.getString("email");
                        var authToken = rs.getString("authtoken");

                        return new UserData(name, password, email, authToken);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email, String authToken) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("INSERT INTO User (username, password, email, authtoken) VALUES(?, ?, ?, ?)")) {
                preparedStatement.setString(1, username);

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashedPassword = encoder.encode(password);

                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, authToken);

                preparedStatement.executeUpdate();

            }

        }
    }

    @Override
    public void updateUser(UserData oldUser, UserData newUser) throws DataAccessException, SQLException{
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("UPDATE User SET username=?, password = ?, email = ?, WHERE authtoken=?")) {
                preparedStatement.setString(1, newUser.username());
                preparedStatement.setString(2, newUser.password());
                preparedStatement.setString(3, newUser.email());
                preparedStatement.setString(4, oldUser.authToken());

                preparedStatement.executeUpdate();
            }

        }
    }

    @Override
    public void deleteUser() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM User")) {
                preparedStatement.executeUpdate();
            }
        }
    }
}
