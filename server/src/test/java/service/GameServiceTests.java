package service;

import dataaccess.*;
import model.GameData;
import org.junit.jupiter.api.*;
import service.requests.CreateGameRequest;
import service.requests.RegisterRequest;
import service.results.CreateGameResult;
import service.results.RegisterResult;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {

    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final UserDAO userDAO = new MemoryUserDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final UserService userService = new UserService(authDAO, userDAO);
    private GameService gameService;
    private final ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);

    @BeforeEach
    public void clear() {
        clearService.clear();
    }

    @Test
    @Order(1)
    public void CreateValidGame() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);
        String authToken = registerResult.authToken();
        gameService = new GameService(authDAO, gameDAO, authToken);
        CreateGameRequest req = new CreateGameRequest("gameName");
        CreateGameResult result = gameService.createGame(req);
        GameData gameData = gameDAO.getGame(result.gameID());
        Assertions.assertNotNull(gameData, "Game not created.");
    }

    @Test
    @Order(2)
    public void CreateInvalidGame() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        userService.register(registerRequest);
        String authToken = "bad_authToken";
        gameService = new GameService(authDAO, gameDAO, authToken);
        CreateGameRequest req = new CreateGameRequest("gameName");
        Assertions.assertThrows(UnauthorizedRequest.class, () -> gameService.createGame(req), "User wasn't flagged as unauthorized.");
    }
}
