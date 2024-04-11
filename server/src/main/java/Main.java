import chess.*;
import dataAccess.DataAccessException;
import server.Server;
import dataAccess.DatabaseManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        //createTables();
        Server server = new Server();
        server.run(8080);
        System.out.println("LECIO ♕ 240 Chess Server");
    }

    }


}