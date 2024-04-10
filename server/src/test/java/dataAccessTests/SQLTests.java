package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;

import model.*;
import org.junit.jupiter.api.*;

import server.Server;
import server.requests.*;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

import static java.util.Collections.sort;


public class SQLTests {

    String username = "leciojor";

    String password = "123";

    String email = "@";



    String token = "dfoom322o3mpom";

    int gameid = 1231241;

    String game_name = "game";

    ChessGame game = new ChessGame();

    String white_username = "white";

    String black_username = "black";

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
        Sql_auth.createAuth(username);
        String token_already_in_database = Sql_auth.getTokenValue(username);
        String current_token = Sql_auth.getCurrentToken(token_already_in_database).authToken();
        Assertions.assertEquals(current_token, token_already_in_database);

    }

    @Test
    @Order(4)
    @DisplayName("Get current token bad")
    public void getCurrentTokenBad() throws Exception{
        String token_already_in_database = Sql_auth.getTokenValue(username);
        AuthData current_token = Sql_auth.getCurrentToken(token_already_in_database);
        Assertions.assertEquals(current_token, null);


    }

    @Test
    @Order(5)
    @DisplayName("Get token value good")
    public void getTokenValueGood() throws Exception {

        Sql_auth.createAuth(username);
        String token_already_in_database = Sql_auth.getTokenValue(username);
        Assertions.assertNotEquals(token_already_in_database, null);

    }

    @Test
    @Order(6)
    @DisplayName("Get token value bad")
    public void getTokenValuebad() throws Exception {

        String token_already_in_database = Sql_auth.getTokenValue(username);
        Assertions.assertNull(token_already_in_database);

    }

    @Test
    @Order(7)
    @DisplayName("Get current auths good")
    public void getCurrentAuthsGood() throws Exception {
        Sql_auth.createAuth(username);
        Sql_auth.createAuth(username + "a");
        Sql_auth.createAuth(username + "b");

        String token_already_in_database = Sql_auth.getTokenValue(username);
        String token_already_in_database_one = Sql_auth.getTokenValue(username + "a");
        String token_already_in_database_two = Sql_auth.getTokenValue(username + "b");

        Vector<AuthData> auths =  Sql_auth.getCurrentAuths();

        AuthData auth_one = new AuthData(token_already_in_database, username);

        AuthData auth_two = new AuthData(token_already_in_database_one, username + "a");

        AuthData auth_three = new AuthData(token_already_in_database_two, username + "b");

        Vector<AuthData> auths_compare = new Vector<>();

        auths_compare.add(auth_one);
        auths_compare.add(auth_two);
        auths_compare.add(auth_three);

        Assertions.assertEquals(auths_compare.size(), auths.size());

    }


    @Test
    @Order(8)
    @DisplayName("Get current auths bad")
    public void getCurrentAuthsBad() throws Exception {


        Vector<AuthData> auths =  Sql_auth.getCurrentAuths();


        Assertions.assertEquals(0, auths.size());

    }

    @Test
    @Order(9)
    @DisplayName("Delete single token good")
    public void deleteSingleTokenGood() throws Exception {
        Sql_auth.createAuth(username);
        String authtoken = Sql_auth.getTokenValue(username);
        AuthData auth = new AuthData(authtoken, username);
        AuthData token = Sql_auth.getCurrentToken(auth.authToken());
        Sql_auth.deleteAuth(token);

        token = Sql_auth.getCurrentToken(auth.authToken());

        Assertions.assertNull(token);

    }


    @Test
    @Order(10)
    @DisplayName("Delete single token bad")
    public void deleteSingleTokenBad() throws Exception {
        Sql_auth.createAuth(username);
        String authtoken = Sql_auth.getTokenValue(username + " sd");
        AuthData auth = new AuthData(authtoken, username);
        Sql_auth.deleteAuth(auth);


        Assertions.assertNotEquals(token, null);


    }

    @Test
    @Order(11)
    @DisplayName("Delete all tokens good")
    public void deleteAllTokensGood() throws Exception {
        Sql_auth.createAuth(username);
        Sql_auth.createAuth(username + "a");
        Sql_auth.createAuth(username + "b");

        Sql_auth.deleteAuthList();

        token = Sql_auth.getTokenValue(username);

        Assertions.assertNull(token);
    }

    @Test
    @Order(12)
    @DisplayName("Delete all tokens bad")
    public void deleteAllTokensBad() throws Exception {

        Sql_auth.deleteAuthList();
        Sql_auth.createAuth(username);
        token = Sql_auth.getTokenValue(username);

        Assertions.assertNotEquals(token, null);
    }

    @Test
    @Order(13)
    @DisplayName("Get user good")
    public void getUserGood() throws Exception{
        Sql_auth.createAuth(username);
        token = Sql_auth.getTokenValue(username);
        Sql_user.createUser(username, password, email, token);
        UserData user_already_in_database = Sql_user.getUser(username);
        Assertions.assertNotEquals(user_already_in_database, null);

    }
    @Test
    @Order(14)
    @DisplayName("Get user bad")
    public void getUserBad() throws Exception{
        token = Sql_auth.getTokenValue(username);
        Sql_user.createUser(username, password, email, token);
        UserData user_already_in_database = Sql_user.getUser(username);
        Assertions.assertEquals(user_already_in_database.authToken(), null);

    }


    @Test
    @Order(15)
    @DisplayName("Create user good")
    public void createUserGood() throws Exception{
        Sql_auth.createAuth(username);
        token = Sql_auth.getTokenValue(username);
        Sql_user.createUser(username, password, email, token);
        UserData user_already_in_database = Sql_user.getUser(username);
        Assertions.assertNotEquals(user_already_in_database, null);
    }
    @Test
    @Order(16)
    @DisplayName("Create user bad")
    public void createUserbad() throws Exception{
        try{
            Sql_user.createUser(username, password, email, token);
            UserData user_already_in_database = Sql_user.getUser(username);
        } catch (SQLIntegrityConstraintViolationException a){
            Assertions.assertThrows(SQLIntegrityConstraintViolationException.class, () -> {
                Sql_user.createUser(username, password, email, token);});

        }

    }

    @Test
    @Order(17)
    @DisplayName("Update user good")
    public void updateUserGood() throws Exception{
        Sql_auth.createAuth(username);
        token = Sql_auth.getTokenValue(username);
        Sql_user.createUser(username, password, email, token);
        UserData user_already_in_database = Sql_user.getUser(username);
        UserData new_user = new UserData(username + "a", password + "a", email + "a", token);

        Sql_user.updateUser(user_already_in_database, new_user);

        UserData user_updated = Sql_user.getUser(username);

        Assertions.assertNotEquals(user_updated, user_already_in_database);

    }

    @Test
    @Order(18)
    @DisplayName("Update user bad")
    public void updateUserBad() throws Exception{

        UserData new_user = new UserData(username + "a", password + "a", email + "a", token);
        UserData new_new_user = new UserData(username + "ab", password + "ab", email + "a", token);

        Sql_user.updateUser(new_user, new_new_user);
        UserData user_updated = Sql_user.getUser(username + "a");
        UserData user_updated_two = Sql_user.getUser(username + "ab");

        Assertions.assertNull(user_updated);
        Assertions.assertNull(user_updated_two);





    }



    @Test
    @Order(19)
    @DisplayName("Delete all users good")
    public void deleteAllUsersGood() throws Exception {
        Sql_auth.createAuth(username);
        Sql_auth.createAuth(username + "a");
        Sql_auth.createAuth(username + "b");

        token = Sql_auth.getTokenValue(username);
        Sql_user.createUser(username, password, email, token);

        String token_two = Sql_auth.getTokenValue(username);
        Sql_user.createUser(username + "a", password, email, token_two);

        String token_three = Sql_auth.getTokenValue(username);
        Sql_user.createUser(username + "b", password, email, token_three);


        Sql_user.deleteUser();

        UserData user = Sql_user.getUser(username);

        Assertions.assertNull(user);

    }

    @Test
    @Order(20)
    @DisplayName("Delete all users bad")
    public void deleteAllUsersBad() throws Exception {
        Sql_user.deleteUser();
        Sql_auth.createAuth(username);
        token = Sql_auth.getTokenValue(username);
        Sql_user.createUser(username, password, email, token);
        UserData user = Sql_user.getUser(username);

        Assertions.assertNotEquals(user, null);
    }

    @Test
    @Order(21)
    @DisplayName("Create game good")
    public void createGameGood() throws Exception {
        Sql_game.createGame(gameid, white_username, black_username, game_name, game);
        GameData game_already_in_database = Sql_game.getGame(game_name);
        Assertions.assertNotEquals(game_already_in_database, null);

    }
    @Test
    @Order(22)
    @DisplayName("Create game bad")
    public void createGameBad() throws Exception {

        try{
            Sql_game.createGame(gameid, white_username, black_username, game_name, game);
            GameData game_already_in_database = Sql_game.getGame(game_name);
        } catch (SQLIntegrityConstraintViolationException a){
            Assertions.assertThrows(SQLIntegrityConstraintViolationException.class, () -> {
                Sql_game.createGame(gameid, white_username, black_username, game_name, game);});

        }

    }


    @Test
    @Order(23)
    @DisplayName("Get game good")
    public void getGameGood() throws Exception {
        Sql_game.createGame(gameid, white_username, black_username, game_name, game);
        GameData game_already_in_database = Sql_game.getGame(game_name);
        Assertions.assertNotEquals(game_already_in_database, null);

    }

    @Test
    @Order(24)
    @DisplayName("Get game bad")
    public void getGameBad() throws Exception {
        GameData game_already_in_database = Sql_game.getGame(game_name);
        Assertions.assertEquals(game_already_in_database, null);

    }

    @Test
    @Order(25)
    @DisplayName("Add user to game good")
    public void addUserToGameGood() throws Exception {
        white_username = null;
        Sql_game.createGame(gameid, white_username, black_username, game_name, game);
        GameData game_already_in_database = Sql_game.getGame(game_name);
        white_username = "username" + "a";
        GameData game_new = new GameData(gameid, white_username, black_username, game_name + "b", game);

        Sql_game.addUser(game_already_in_database, game_new);

        GameData game_updated = Sql_game.getGame(game_name);

        Assertions.assertNotEquals(game_updated, game_already_in_database);

    }

    @Test
    @Order(26)
    @DisplayName("Add user to game bad")
    public void addUserToGameBad() throws Exception {
        white_username = "username" + "a";
        Sql_game.createGame(gameid, white_username, black_username, game_name, game);
        GameData game_already_in_database = Sql_game.getGame(game_name);

        GameData game_new = new GameData(gameid, white_username, black_username, game_name + "b", game);

        Sql_game.addUser(game_already_in_database, game_new);

        GameData game_updated = Sql_game.getGame(game_name);

        Assertions.assertEquals(game_updated, null);

    }

    @Test
    @Order(27)
    @DisplayName("Get game by id good")
    public void getGameByIdGood() throws Exception{
        Sql_game.createGame(gameid, white_username, black_username, game_name, game);
        GameData game_already_in_database = Sql_game.getGameByID(Integer.toString(gameid));
        Assertions.assertNotEquals(game_already_in_database, null);
    }

    @Test
    @Order(28)
    @DisplayName("Get game by id bad")
    public void getGameById() throws Exception{
        GameData game_already_in_database = Sql_game.getGameByID(Integer.toString(gameid));
        Assertions.assertNull(game_already_in_database);
    }

    @Test
    @Order(29)
    @DisplayName("Get games list good")
    public void getGamesListGood() throws Exception{
        Sql_game.createGame(gameid, white_username, black_username, game_name, game);
        ChessGame new_game = new ChessGame();
        Sql_game.createGame(gameid + 123, white_username + "a", black_username + "a", game_name = "a", new_game);

        HashSet<GameData> games =  Sql_game.getListGames();

        Assertions.assertNotEquals(games, null);

    }

    @Test
    @Order(30)
    @DisplayName("games list bad")
    public void getGamesListBad() throws Exception{
        HashSet<GameData> games =  Sql_game.getListGames();

        Assertions.assertTrue(games.isEmpty());
    }

    @Test
    @Order(31)
    @DisplayName("Delete all games good")
    public void deleteAllGamesGood() throws Exception{
        Sql_game.createGame(gameid, white_username, black_username, game_name, game);
        ChessGame new_game = new ChessGame();
        Sql_game.createGame(gameid + 123, white_username + "a", black_username + "a", game_name + "a", new_game);

        Sql_game.deleteGame();

        GameData game = Sql_game.getGame(game_name);

        Assertions.assertNull(game);
    }

    @Test
    @Order(32)
    @DisplayName("Delete all games bad")
    public void deleteAllGamesBad() throws Exception{
        Sql_game.deleteGame();
        Sql_game.createGame(gameid, white_username, black_username, game_name, game);
        GameData game = Sql_game.getGame(game_name);

        Assertions.assertNotEquals(game, null);
    }




}
