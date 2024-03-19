package clientTests;

import client.ClientCommunicator;
import client.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;
import services.ClearService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

public class ServerFacadeTests {

    private static Server server;

    private static ServerFacade server_call = new ServerFacade();

    private String getGameId(String input){
        String[] lines = input.split("\n");
        String gameId = null;
        for (String line : lines) {
            if (line.startsWith("GameID:")) {
                gameId = line.substring(line.indexOf(":") + 1).trim();
                break;
            }
        }

        return gameId;

    }

    @BeforeAll
    public static void init() throws SQLException {
        server = new Server();
        var port = server.run(8080);
        ClearService clearing = new ClearService();
        clearing.clear();
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void initEach() throws SQLException {
        ClearService clearing = new ClearService();
        clearing.clear();
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    @DisplayName("Register Good")
    public void registerGood() throws Exception{
        server_call.register("lecio 123 leciojor@gmail.com");
        Assertions.assertFalse(ServerFacade.returned_error);
    }

    @Test
    @Order(2)
    @DisplayName("Register Bad")
    public void registerBad() throws Exception{
        server_call.register("lecio 123 leciojor@gmail.com");
        server_call.register("lecio 123 leciojor@gmail.com");
        Assertions.assertTrue(ServerFacade.returned_error);

    }

    @Test
    @Order(3)
    @DisplayName("Login Good")
    public void loginGood() throws Exception{
        server_call.register("lecio 123 leciojor@gmail.com");
        server_call.login("lecio 123");
        Assertions.assertFalse(ServerFacade.returned_error);
    }

    @Test
    @Order(4)
    @DisplayName("Login Bad")
    public void loginBad() throws Exception{
        server_call.login("lecio 123");
        Assertions.assertTrue(ServerFacade.returned_error);
    }

    @Test
    @Order(5)
    @DisplayName("Logout Good")
    public void logoutGood() throws Exception{
        server_call.register("lecio 123 leciojor@gmail.com");
        server_call.logout();
        Assertions.assertFalse(ServerFacade.returned_error);
    }

    @Test
    @Order(6)
    @DisplayName("Logout Bad")
    public void logoutBad() throws Exception{
        ClientCommunicator.current_auth_token = "123";
        server_call.logout();
        Assertions.assertTrue(ServerFacade.returned_error);
    }


    @Test
    @Order(7)
    @DisplayName("Create Good")
    public void createGood() throws Exception{
        server_call.register("lecio 123 leciojor@gmail.com");
        server_call.create("game");
        Assertions.assertFalse(ServerFacade.returned_error);
    }

    @Test
    @Order(8)
    @DisplayName("Create Bad")
    public void createBad() throws Exception{
        server_call.create("game");
        Assertions.assertTrue(ServerFacade.returned_error);

    }

    @Test
    @Order(9)
    @DisplayName("List Good")
    public void listGood() throws Exception{
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        server_call.register("lecio 123 leciojor@gmail.com");
        server_call.create("game");
        server_call.list();
        String printedOutput = outputStreamCaptor.toString();
        int start_index = printedOutput.indexOf("0. ");
        String result_string = start_index != -1 ? printedOutput.substring(start_index, printedOutput.indexOf("\n", start_index)).trim() : null;


        Assertions.assertEquals(result_string, "0. game");
    }

    @Test
    @Order(10)
    @DisplayName("List Bad")
    public void listBad() throws Exception{
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        server_call.register("lecio 123 leciojor@gmail.com");
        server_call.list();
        String printedOutput = outputStreamCaptor.toString();
        int start_index = printedOutput.indexOf("0. ");
        String result_string = start_index != -1 ? printedOutput.substring(start_index, printedOutput.indexOf("\n", start_index)).trim() : null;


        Assertions.assertNotEquals(result_string, "0. game");
    }

    @Test
    @Order(11)
    @DisplayName("Clear Good")
    public void clearGood() throws Exception{
        server_call.register("lecio 123 leciojor@gmail.com");
        server_call.clear();
        server_call.create("game");
        Assertions.assertTrue(ServerFacade.returned_error);
    }

    @Test
    @Order(12)
    @DisplayName("Clear Bad")
    public void clearBad() throws Exception{
        //there isn't really a possible scenario for that one
    }

    @Test
    @Order(13)
    @DisplayName("Join Good")
    public void joinGood() throws Exception{
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        server_call.register("lecio 123 a");
        server_call.create("game");

        String printedOutput = outputStreamCaptor.toString();
        String id = getGameId(printedOutput);
        server_call.join(id);
        Assertions.assertFalse(ServerFacade.returned_error);
    }

    @Test
    @Order(14)
    @DisplayName("Join Bad")
    public void joinBad() throws Exception{

        server_call.register("lecio 123 a");
        server_call.create("game");

        server_call.join("123 BLACK");
        Assertions.assertTrue(ServerFacade.returned_error);
    }


}