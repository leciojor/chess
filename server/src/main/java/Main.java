import chess.*;
import dataAccess.DataAccessException;
import server.Server;
import dataAccess.DatabaseManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        createTables();
        Server server = new Server();
        server.run(8080);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
    }


    private static void createTables() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            var createUserTable = """
            CREATE TABLE  IF NOT EXISTS User (
                id INT NOT NULL AUTO_INCREMENT,
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                authtoken VARCHAR(255) NOT NULL,
                FOREIGN KEY (authtoken) REFERENCES Auth(authtoken),
                PRIMARY KEY (id)
            )""";

            var createAuthTable = """
            CREATE TABLE  IF NOT EXISTS Auth (
                authtoken VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL,
                PRIMARY KEY (authtoken)
            )""";

            var createGameTable = """
            CREATE TABLE  IF NOT EXISTS Game (
                gameid INT NOT NULL,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255),
                game TEXT NOT NULL,
                PRIMARY KEY (gameid)
            )""";

            try (var createTableStatement = conn.prepareStatement(createAuthTable)) {
                createTableStatement.executeUpdate();
            }

            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                createTableStatement.executeUpdate();
            }

            try (var createTableStatement = conn.prepareStatement(createGameTable)) {
                createTableStatement.executeUpdate();
            }
        }


    }


}