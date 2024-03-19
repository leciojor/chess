package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import server.responses.RegisterResponse;
import services.ClearService;
import services.RegisterService;

import java.sql.SQLException;

public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() throws SQLException {
        server = new Server();
        var port = server.run(8080);
        ClearService clearing = new ClearService();
        clearing.clear();
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    @DisplayName("Register Good")
    public void registerGood() throws Exception{

    }

    @Test
    @Order(2)
    @DisplayName("Register Bad")
    public void registerBad() throws Exception{

    }

    @Test
    @Order(3)
    @DisplayName("Login Good")
    public void loginGood() throws Exception{

    }

    @Test
    @Order(4)
    @DisplayName("Login Bad")
    public void loginBad() throws Exception{

    }

    @Test
    @Order(5)
    @DisplayName("Logout Good")
    public void logoutGood() throws Exception{

    }

    @Test
    @Order(6)
    @DisplayName("Logout Bad")
    public void logoutBad() throws Exception{

    }

    @Test
    @Order(7)
    @DisplayName("List Good")
    public void listGood() throws Exception{

    }

    @Test
    @Order(8)
    @DisplayName("List Bad")
    public void listBad() throws Exception{

    }

    @Test
    @Order(9)
    @DisplayName("Create Good")
    public void createGood() throws Exception{

    }

    @Test
    @Order(10)
    @DisplayName("Create Bad")
    public void createBad() throws Exception{

    }

    @Test
    @Order(11)
    @DisplayName("Clear Good")
    public void clearGood() throws Exception{

    }

    @Test
    @Order(12)
    @DisplayName("Clear Bad")
    public void clearBad() throws Exception{

    }

    @Test
    @Order(13)
    @DisplayName("Join Good")
    public void joinGood() throws Exception{

    }

    @Test
    @Order(14)
    @DisplayName("Join Bad")
    public void joinBad() throws Exception{

    }


}