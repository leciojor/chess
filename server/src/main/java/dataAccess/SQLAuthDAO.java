package dataAccess;

import model.AuthData;

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
                        var authtoken = rs.getString("authtoken");

                        return new AuthData(authtoken, name);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getTokenValue(String username) throws DataAccessException, SQLException {

        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT username, authtoken, created_at FROM Auth WHERE username=? ORDER BY created_at DESC LIMIT 1")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var authtoken = rs.getString("authtoken");
                        return authtoken;
                    }
                }
            }
        }
        return null;
    }


    @Override
    public Vector<AuthData> getCurrentAuths() throws DataAccessException, SQLException {
        Vector<AuthData> currentAuths = new Vector<AuthData>();
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("SELECT authtoken, username FROM Auth")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var name = rs.getString("username");
                        var authtoken = rs.getString("authtoken");

                        AuthData auth = new AuthData(authtoken, name);
                        currentAuths.add(auth);
                    }
                }
            }
        }
        return currentAuths;
    }

    @Override
    public void createAuth(String username) throws DataAccessException, SQLException {
        String randomToken = UUID.randomUUID().toString();
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("UPDATE User SET authtoken = null WHERE username = ?")){
                preparedStatement.setString(1, username);
                preparedStatement.executeUpdate();
            }

            try (var preparedStatement = conn.prepareStatement("INSERT INTO Auth (authtoken, username) VALUES(?, ?)")) {

                preparedStatement.setString(1, randomToken);
                preparedStatement.setString(2, username);

                preparedStatement.executeUpdate();

            }
            try (var preparedStatement = conn.prepareStatement("UPDATE User SET authtoken = ? WHERE username = ?")) {
                preparedStatement.setString(1, randomToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public void deleteAuth(AuthData token) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE User SET authtoken = null WHERE authtoken = ?")){
                preparedStatement.setString(1, token.authToken());
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement("DELETE FROM Auth WHERE authtoken=?")) {
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
