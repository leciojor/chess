package dataAccessTests;

import dataAccess.*;

import org.junit.jupiter.api.*;

import server.Server;
import server.requests.*;
import server.responses.RegisterResponse;
import services.*;

import java.sql.SQLException;
import java.util.UUID;


public class SQLTests {

    String username = "leciojor";

    SQLAuthDAO Sql_auth = new SQLAuthDAO();
    SQLUserDAO Sql_user = new SQLUserDAO();
    SQLGameDAO Sql_game = new SQLGameDAO();

    @BeforeEach
    public void setup() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            var dropOne = "DROP TABLE User";
            var dropTwo = "DROP TABLE Auth";
            var dropThree = "DROP TABLE Game";
            try (var createTableStatement = conn.prepareStatement(dropOne)) {
                createTableStatement.executeUpdate();
            }

            try (var createTableStatement = conn.prepareStatement(dropTwo)) {
                createTableStatement.executeUpdate();
            }

            try (var createTableStatement = conn.prepareStatement(dropThree)) {
                createTableStatement.executeUpdate();
            }
        }

        DatabaseManager.createDatabase();
        Server.createTables();



    }


    @Test
    @Order(1)
    @DisplayName("Create token good")
    public void createTokenGood() throws Exception{
        Sql_auth.createAuth(username);
        String token_already_in_database = Sql_auth.getTokenValue(username);
        Assertions.assertNotEquals(token_already_in_database, null);
    }


    @Test
    @Order(2)
    @DisplayName("Create token bad")
    public void createTokenBad() throws Exception{
        String token_already_in_database = Sql_auth.getTokenValue(username);
        Assertions.assertEquals(token_already_in_database, null);
    }

    @Test
    @Order(3)
    @DisplayName("Get current token good")
    public void getCurrentTokenGood() throws Exception{
        Assertions.assertEquals(200, response_login.getStatus());

    }
//
//    @Test
//    @Order(4)
//    @DisplayName("Login 401")
//    public void loginBad() throws Exception{
//        request_login = new LoginRequest(username_created, "123123123");
//        LoginService service_login = new LoginService(request_login);
//        response_login = service_login.login(username_created, "123123123");
//        Assertions.assertEquals(401, response_login.getStatus());
//
//    }
//
//    @Test
//    @Order(5)
//    @DisplayName("Logout 200")
//    public void logoutGood() throws Exception {
//
//        request_logout = new LogoutRequest(within_token);
//        LogoutService service_logout = new LogoutService(request_logout);
//        response_logout = service_logout.logout(within_token);
//        Assertions.assertEquals(200, response_logout.getStatus());
//
//    }
//
//
//
//
//    @Test
//    @Order(6)
//    @DisplayName("Logout 401")
//    public void logoutBad() throws Exception {
//        request_logout = new LogoutRequest(token);
//        LogoutService service_logout = new LogoutService(request_logout);
//        response_logout = service_logout.logout(token);
//        Assertions.assertEquals(401, response_logout.getStatus());
//
//    }
//
//    @Test
//    @Order(7)
//    @DisplayName("Create Game 200")
//    public void createGameGood() throws Exception {
//        request_create_game = new CreateGameRequest(game_name + "123");
//        CreateGameService service_create_game = new CreateGameService(request_create_game);
//        response_create_game = service_create_game.createGame(game_name + "123", within_token);
//        Assertions.assertEquals(200, response_create_game.getStatus());
//
//    }
//
//    @Test
//    @Order(8)
//    @DisplayName("Create Game 401")
//    public void createGameBad() throws Exception {
//        request_create_game = new CreateGameRequest(game_name);
//        CreateGameService service_create_game = new CreateGameService(request_create_game);
//        response_create_game = service_create_game.createGame(game_name, token);
//        Assertions.assertEquals(401, response_create_game.getStatus());
//
//    }
//
//    @Test
//    @Order(9)
//    @DisplayName("Join Game 200")
//    public void joinGameGood() throws Exception {
//        request_join_game = new JoinGameRequest(color_good, String.valueOf(gameID_right));
//        JoinGameService service_create_game = new JoinGameService(request_join_game);
//        response_join_game = service_create_game.joinGame(color_good, String.valueOf(gameID_right), within_token);
//        Assertions.assertEquals(200, response_join_game.getStatus());
//
//    }
//
//
//    @Test
//    @Order(10)
//    @DisplayName("Join Game 400")
//    public void joinGameBad() throws Exception {
//        request_join_game = new JoinGameRequest(color_good, String.valueOf(gameID));
//        JoinGameService service_create_game = new JoinGameService(request_join_game);
//        response_join_game = service_create_game.joinGame(color_good, String.valueOf(gameID), token);
//        Assertions.assertEquals(400, response_join_game.getStatus());
//
//    }
//
//    @Test
//    @Order(11)
//    @DisplayName("List Games 200")
//    public void listGamesGood() throws Exception {
//        request_list_games = new ListGamesRequest(within_token);
//        ListGamesService service_list_games = new ListGamesService(request_list_games);
//        response_list_games = service_list_games.listGames(within_token);
//        Assertions.assertEquals(200, response_list_games.getStatus());
//
//    }
//
//    @Test
//    @Order(12)
//    @DisplayName("List Games 401")
//    public void listGamesBad() throws Exception {
//        request_list_games = new ListGamesRequest(token);
//        ListGamesService service_list_games = new ListGamesService(request_list_games);
//        response_list_games = service_list_games.listGames(token);
//        Assertions.assertEquals(401, response_list_games.getStatus());
//
//    }
//
//    @Test
//    @Order(13)
//    @DisplayName("Clear")
//    public void clear() throws Exception{
//        ClearService service_clear = new ClearService();
//        response_clear = service_clear.clear();
//        Assertions.assertEquals(200, response_clear.getStatus());
//    }
//
//    @Test
//    @Order(14)
//    @DisplayName("Register 200")
//    public void registerGood() throws Exception{
//        Assertions.assertEquals(200, response_register.getStatus());
//    }
//
//
//    @Test
//    @Order(15)
//    @DisplayName("Register 403")
//    public void registerBad() throws Exception{
//        RegisterService service = new RegisterService(request_register);
//        RegisterResponse response = service.register(username_created, password, email);
//        Assertions.assertEquals(403, response.getStatus());
//    }
//
//    @Test
//    @Order(16)
//    @DisplayName("Login 200")
//    public void loginGood() throws Exception{
//        Assertions.assertEquals(200, response_login.getStatus());
//
//    }
//
//    @Test
//    @Order(17)
//    @DisplayName("Login 401")
//    public void loginBad() throws Exception{
//        request_login = new LoginRequest(username_created, "123123123");
//        LoginService service_login = new LoginService(request_login);
//        response_login = service_login.login(username_created, "123123123");
//        Assertions.assertEquals(401, response_login.getStatus());
//
//    }
//
//    @Test
//    @Order(18)
//    @DisplayName("Logout 200")
//    public void logoutGood() throws Exception {
//
//        request_logout = new LogoutRequest(within_token);
//        LogoutService service_logout = new LogoutService(request_logout);
//        response_logout = service_logout.logout(within_token);
//        Assertions.assertEquals(200, response_logout.getStatus());
//
//    }
//
//
//
//
//    @Test
//    @Order(19)
//    @DisplayName("Logout 401")
//    public void logoutBad() throws Exception {
//        request_logout = new LogoutRequest(token);
//        LogoutService service_logout = new LogoutService(request_logout);
//        response_logout = service_logout.logout(token);
//        Assertions.assertEquals(401, response_logout.getStatus());
//
//    }
//
//    @Test
//    @Order(20)
//    @DisplayName("Create Game 200")
//    public void createGameGood() throws Exception {
//        request_create_game = new CreateGameRequest(game_name + "123");
//        CreateGameService service_create_game = new CreateGameService(request_create_game);
//        response_create_game = service_create_game.createGame(game_name + "123", within_token);
//        Assertions.assertEquals(200, response_create_game.getStatus());
//
//    }
//
//    @Test
//    @Order(21)
//    @DisplayName("Create Game 401")
//    public void createGameBad() throws Exception {
//        request_create_game = new CreateGameRequest(game_name);
//        CreateGameService service_create_game = new CreateGameService(request_create_game);
//        response_create_game = service_create_game.createGame(game_name, token);
//        Assertions.assertEquals(401, response_create_game.getStatus());
//
//    }
//
//    @Test
//    @Order(22)
//    @DisplayName("Join Game 200")
//    public void joinGameGood() throws Exception {
//        request_join_game = new JoinGameRequest(color_good, String.valueOf(gameID_right));
//        JoinGameService service_create_game = new JoinGameService(request_join_game);
//        response_join_game = service_create_game.joinGame(color_good, String.valueOf(gameID_right), within_token);
//        Assertions.assertEquals(200, response_join_game.getStatus());
//
//    }
//
//
//    @Test
//    @Order(23)
//    @DisplayName("Join Game 400")
//    public void joinGameBad() throws Exception {
//        request_join_game = new JoinGameRequest(color_good, String.valueOf(gameID));
//        JoinGameService service_create_game = new JoinGameService(request_join_game);
//        response_join_game = service_create_game.joinGame(color_good, String.valueOf(gameID), token);
//        Assertions.assertEquals(400, response_join_game.getStatus());
//
//    }
//
//    @Test
//    @Order(24)
//    @DisplayName("List Games 200")
//    public void listGamesGood() throws Exception {
//        request_list_games = new ListGamesRequest(within_token);
//        ListGamesService service_list_games = new ListGamesService(request_list_games);
//        response_list_games = service_list_games.listGames(within_token);
//        Assertions.assertEquals(200, response_list_games.getStatus());
//
//    }
//
//    @Test
//    @Order(25)
//    @DisplayName("List Games 401")
//    public void listGamesBad() throws Exception {
//        request_list_games = new ListGamesRequest(token);
//        ListGamesService service_list_games = new ListGamesService(request_list_games);
//        response_list_games = service_list_games.listGames(token);
//        Assertions.assertEquals(401, response_list_games.getStatus());
//
//    }
//
//    @Test
//    @Order(26)
//    @DisplayName("Clear")
//    public void clear() throws Exception{
//        ClearService service_clear = new ClearService();
//        response_clear = service_clear.clear();
//        Assertions.assertEquals(200, response_clear.getStatus());
//    }
//
//    @Test
//    @Order(27)
//    @DisplayName("Clear")
//    public void clear() throws Exception{
//        ClearService service_clear = new ClearService();
//        response_clear = service_clear.clear();
//        Assertions.assertEquals(200, response_clear.getStatus());
//    }
//
//    @Test
//    @Order(28)
//    @DisplayName("Clear")
//    public void clear() throws Exception{
//        ClearService service_clear = new ClearService();
//        response_clear = service_clear.clear();
//        Assertions.assertEquals(200, response_clear.getStatus());
//    }
//
//    @Test
//    @Order(29)
//    @DisplayName("Clear")
//    public void clear() throws Exception{
//        ClearService service_clear = new ClearService();
//        response_clear = service_clear.clear();
//        Assertions.assertEquals(200, response_clear.getStatus());
//    }
//
//    @Test
//    @Order(30)
//    @DisplayName("Clear")
//    public void clear() throws Exception{
//        ClearService service_clear = new ClearService();
//        response_clear = service_clear.clear();
//        Assertions.assertEquals(200, response_clear.getStatus());
//    }
//
//    @Test
//    @Order(31)
//    @DisplayName("Clear")
//    public void clear() throws Exception{
//        ClearService service_clear = new ClearService();
//        response_clear = service_clear.clear();
//        Assertions.assertEquals(200, response_clear.getStatus());
//    }
//
//    @Test
//    @Order(32)
//    @DisplayName("Clear")
//    public void clear() throws Exception{
//        ClearService service_clear = new ClearService();
//        response_clear = service_clear.clear();
//        Assertions.assertEquals(200, response_clear.getStatus());
//    }
//
//


}
