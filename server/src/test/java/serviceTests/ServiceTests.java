package serviceTests;

import dataAccess.*;
import services.*;
import server.*;
import org.junit.jupiter.api.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ServiceTests {


    private String username = "lecio";

    private String username_created = "bia";

    private String password = "123";
    
    private String email = "yeah@gmail.com";

    private String token = "12344";

    private String within_token;

    private String game_name = "game";

    private String color_good = "WHITE";

    private String color_spectador = "asdas";

    private int gameID_right = 123123;

    private int gameID = 12;

    private UserDAO user;

    private AuthDAO auth;

    private GameDAO game;

    RegisterRequest request_register;

    RegisterResponse response_register;

    LoginRequest request_login;

    LoginResponse response_login;

    LogoutRequest request_logout;

    LogoutResponse response_logout;

    CreateGameRequest request_create_game;

    CreateGameResponse response_create_game;

    JoinGameRequest request_join_game;

    JoinGameResponse response_join_game;

    ListGamesRequest request_list_games;

    ListGamesResponse response_list_games;

    ClearResponse response_clear;
    

    @BeforeEach
    public void setup(){
        user = new MemoryUserDAO();

        auth = new MemoryAuthDAO();

        game = new MemoryGameDAO();


        request_register = new RegisterRequest(username_created, password, email);
        RegisterService service_register = new RegisterService(request_register);
        response_register = service_register.register(username_created, password, email);

        request_login = new LoginRequest(username_created, password);
        LoginService service_login = new LoginService(request_login);
        response_login = service_login.login(username_created, password);

        auth.createAuth(username);
        within_token = auth.getCurrentAuths().get(auth.getCurrentAuths().size() - 1).authToken();

        game.createGame(gameID_right, "", "", game_name, null);




    }

    @Test
    @Order(1)
    @DisplayName("Register 200")
    public void registerGood() throws Exception{
        Assertions.assertEquals(200, response_register.getStatus());
    }


    @Test
    @Order(2)
    @DisplayName("Register 403")
    public void registerBad() throws Exception{
        RegisterService service = new RegisterService(request_register);
        RegisterResponse response = service.register(username_created, password, email);
        Assertions.assertEquals(403, response.getStatus());
    }

    @Test
    @Order(3)
    @DisplayName("Login 200")
    public void loginGood() throws Exception{
        Assertions.assertEquals(200, response_login.getStatus());

    }

    @Test
    @Order(4)
    @DisplayName("Login 401")
    public void loginBad() throws Exception{
        request_login = new LoginRequest(username_created, "123123123");
        LoginService service_login = new LoginService(request_login);
        response_login = service_login.login(username_created, "123123123");
        Assertions.assertEquals(401, response_login.getStatus());

    }

    @Test
    @Order(5)
    @DisplayName("Logout 200")
    public void logoutGood() throws Exception {

        request_logout = new LogoutRequest(within_token);
        LogoutService service_logout = new LogoutService(request_logout);
        response_logout = service_logout.logout(within_token);
        Assertions.assertEquals(200, response_logout.getStatus());

    }




    @Test
    @Order(6)
    @DisplayName("Logout 401")
    public void logoutBad() throws Exception {
        request_logout = new LogoutRequest(token);
        LogoutService service_logout = new LogoutService(request_logout);
        response_logout = service_logout.logout(token);
        Assertions.assertEquals(401, response_logout.getStatus());

    }

    @Test
    @Order(7)
    @DisplayName("Create Game 200")
    public void createGameGood() throws Exception {
        request_create_game = new CreateGameRequest(game_name + "123");
        CreateGameService service_create_game = new CreateGameService(request_create_game);
        response_create_game = service_create_game.createGame(game_name + "123", within_token);
        Assertions.assertEquals(200, response_create_game.getStatus());

    }

    @Test
    @Order(8)
    @DisplayName("Create Game 401")
    public void createGameBad() throws Exception {
        request_create_game = new CreateGameRequest(game_name);
        CreateGameService service_create_game = new CreateGameService(request_create_game);
        response_create_game = service_create_game.createGame(game_name, token);
        Assertions.assertEquals(401, response_create_game.getStatus());

    }

    @Test
    @Order(9)
    @DisplayName("Join Game 200")
    public void joinGameGood() throws Exception {
        request_join_game = new JoinGameRequest(color_good, String.valueOf(gameID_right));
        JoinGameService service_create_game = new JoinGameService(request_join_game);
        response_join_game = service_create_game.joinGame(color_good, String.valueOf(gameID_right), within_token);
        Assertions.assertEquals(200, response_join_game.getStatus());

    }


    @Test
    @Order(10)
    @DisplayName("Join Game 400")
    public void joinGameBad() throws Exception {
        request_join_game = new JoinGameRequest(color_good, String.valueOf(gameID));
        JoinGameService service_create_game = new JoinGameService(request_join_game);
        response_join_game = service_create_game.joinGame(color_good, String.valueOf(gameID), token);
        Assertions.assertEquals(400, response_join_game.getStatus());

    }

    @Test
    @Order(11)
    @DisplayName("List Games 200")
    public void listGamesGood() throws Exception {
        request_list_games = new ListGamesRequest(within_token);
        ListGamesService service_list_games = new ListGamesService(request_list_games);
        response_list_games = service_list_games.listGames(within_token);
        Assertions.assertEquals(200, response_list_games.getStatus());

    }

    @Test
    @Order(12)
    @DisplayName("List Games 401")
    public void listGamesBad() throws Exception {
        request_list_games = new ListGamesRequest(token);
        ListGamesService service_list_games = new ListGamesService(request_list_games);
        response_list_games = service_list_games.listGames(token);
        Assertions.assertEquals(401, response_list_games.getStatus());

    }

    @Test
    @Order(13)
    @DisplayName("Clear")
    public void clear() throws Exception{
        ClearService service_clear = new ClearService();
        response_clear = service_clear.clear();
        Assertions.assertEquals(200, response_clear.getStatus());
    }

}
