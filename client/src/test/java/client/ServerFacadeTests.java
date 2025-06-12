package client;

import dataaccess.mysql.MySQLUserDAO;
import org.junit.jupiter.api.*;
import server.Server;
import service.ClearService;

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
        var clearService = new ClearService();
        clearService.clear();
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

}
