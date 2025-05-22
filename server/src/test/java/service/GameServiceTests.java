package service;

import dataaccess.*;
import model.GameData;
import org.junit.jupiter.api.*;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.requests.RegisterRequest;
import service.results.CreateGameResult;
import service.results.ListGamesResult;
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

    @Test
    @Order(3)
    public void ListGamesSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);
        String authToken = registerResult.authToken();
        gameService = new GameService(authDAO, gameDAO, authToken);
        CreateGameRequest req = new CreateGameRequest("gameName");
        gameService.createGame(req);
        req = new CreateGameRequest("gameName2");
        gameService.createGame(req);
        ListGamesResult result = gameService.listGames();
        Assertions.assertNotNull(result, "ListGamesResult is null.");
    }

    @Test
    @Order(4)
    public void ListGamesInvalid() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        userService.register(registerRequest);
        String authToken = "bad_authToken";
        gameService = new GameService(authDAO, gameDAO, authToken);
        Assertions.assertThrows(UnauthorizedRequest.class, () -> gameService.listGames(), "User wasn't flagged as unauthorized.");
    }

    @Test
    @Order(5)
    public void JoinGameValid() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);
        String authToken = registerResult.authToken();
        gameService = new GameService(authDAO, gameDAO, authToken);
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        JoinGameRequest req = new JoinGameRequest("WHITE", createGameResult.gameID());
        gameService.joinGame(req);
        GameData gameData = gameDAO.getGame(createGameResult.gameID());
        Assertions.assertEquals("username", gameData.whiteUsername());
    }

    @Test
    @Order(6)
    public void JoinGameInvalid() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);
        String authToken = registerResult.authToken();
        gameService = new GameService(authDAO, gameDAO, authToken);
        CreateGameRequest createGameRequest = new CreateGameRequest("gameName");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        JoinGameRequest req = new JoinGameRequest("WHITE", createGameResult.gameID());
        gameService.joinGame(req);
        Assertions.assertThrows(AlreadyTaken.class, () -> gameService.joinGame(req));
    }
}
