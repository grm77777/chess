package client;

import dataaccess.mysql.MySQLGameDAO;
import dataaccess.mysql.MySQLUserDAO;
import org.junit.jupiter.api.*;
import server.Server;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverURL = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverURL);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setup() {
        serverFacade.clear();
    }

    @Test
    public void registerRequest() {
        serverFacade.register("username", "password", "email");
        var userDAO = new MySQLUserDAO();
        var actual = userDAO.getUser("username").username();
        var expected = "username";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void registerAlreadyTaken() {
        serverFacade.register("username", "password", "email");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register(
                "username", "password", "email"));
    }

    @Test
    public void loginSuccessful() {
        serverFacade.register("username", "password", "email");
        Assertions.assertDoesNotThrow(() -> serverFacade.login("username", "password"));
    }

    @Test
    public void loginIncorrectPassword() {
        serverFacade.register("username", "password", "email");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.login("username", "bad_password"));
    }

    @Test
    public void logoutSuccessful() {
        serverFacade.register("username", "password", "email");
        var result = serverFacade.login("username", "password");
        String authToken = result.authToken();
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(authToken));
    }

    @Test
    public void logoutBadToken() {
        String authToken = "bad_token";
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.logout(authToken));
    }

    @Test
    public void createGameSuccessful() {
        serverFacade.register("username", "password", "email");
        var result = serverFacade.login("username", "password");
        String authToken = result.authToken();
        serverFacade.createGame(authToken, "game1");
        var games = serverFacade.listGames(authToken);
        String gameName = games.get(0).gameName();
        Assertions.assertEquals("game1", gameName);
    }

    @Test
    public void createGameBadToken() {
        String authToken = "bad_token";
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.createGame(authToken, "game1"));
    }

    @Test
    public void listGamesSuccessful() {
        serverFacade.register("username", "password", "email");
        var result = serverFacade.login("username", "password");
        String authToken = result.authToken();
        serverFacade.createGame(authToken, "game1");
        serverFacade.createGame(authToken, "game2");
        serverFacade.createGame(authToken, "game3");
        var games = serverFacade.listGames(authToken);
        Assertions.assertEquals(3, games.size());
    }

    @Test
    public void listGamesBadToken() {
        String authToken = "bad_token";
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.listGames(authToken));
    }

    @Test
    public void joinGameSuccessful() {
        serverFacade.register("username", "password", "email");
        var result = serverFacade.login("username", "password");
        String authToken = result.authToken();
        serverFacade.createGame(authToken, "game1");
        var games = serverFacade.listGames(authToken);
        int gameID = games.get(0).gameID();
        serverFacade.joinGame(authToken, "WHITE", gameID);
        var gameDAO = new MySQLGameDAO();
        var gameData = gameDAO.getGame(gameID);
        Assertions.assertEquals("username", gameData.whiteUsername());
    }

    @Test
    public void joinGameUnsuccessful() {
        serverFacade.register("username", "password", "email");
        var result = serverFacade.login("username", "password");
        String authToken = result.authToken();
        serverFacade.createGame(authToken, "game1");
        var games = serverFacade.listGames(authToken);
        int gameID = games.get(0).gameID();
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame("bad_auth", "WHITE", gameID));
    }
}
