package dataAccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;
import java.util.Vector;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public AuthData getCurrentToken(String token) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT authtoken, username FROM Auth WHERE authtoken=?")) {
                preparedStatement.setString(1, token);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var name = rs.getString("username");

                        return new AuthData(token, name);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Vector<AuthData> getCurrentAuths() throws DataAccessException, SQLException {
        Vector<AuthData> current_auths = new Vector<AuthData>();
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT authtoken, username FROM Auth")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var name = rs.getString("username");
                        var authtoken = rs.getString("authtoken");

                        AuthData auth = new AuthData(authtoken, name);
                        current_auths.add(auth);
                    }
                }
            }
        }
        return current_auths;
    }

    @Override
    public void createAuth(String username) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("INSERT INTO Auth (authtoken, username) VALUES(?, ?)")) {
                String randomToken = UUID.randomUUID().toString();
                preparedStatement.setString(1, randomToken);
                preparedStatement.setString(2, username);

                preparedStatement.executeUpdate();

            }

        }
    }

    @Override
    public void deleteAuth(AuthData token) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM User WHERE authtoken=?")) {
                preparedStatement.setString(1, token.authToken());
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public void deleteAuthList() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM Auth")) {
                preparedStatement.executeUpdate();
            }
        }

    }
}
