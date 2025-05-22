package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.results.LoginResult;
import service.results.LogoutResult;
import service.results.RegisterResult;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {

    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final UserDAO userDAO = new MemoryUserDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final UserService userService = new UserService(authDAO, userDAO);
    private final ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);

    @BeforeEach
    public void clear() {
        clearService.clear();
    }

    @Test
    @Order(1)
    public void RegisterValidInput() {
        RegisterRequest req = new RegisterRequest("username", "password", "email");
        userService.register(req);
        UserDAO userDAO = userService.getUserDAO();
        UserData userData = userDAO.getUser("username");
        Assertions.assertNotNull(userData, "User not registered in database.");
    }

    @Test
    @Order(2)
    public void RegisterPreviousUser() {
        RegisterRequest req = new RegisterRequest("username", "password", "email");
        try {
            userService.register(req);
        } catch (AlreadyTakenException ignored) {}
        Assertions.assertThrows(AlreadyTakenException.class, () -> userService.register(req), "Username wasn't registered as already taken.");
    }

    @Test
    @Order(3)
    public void LoginUserSuccess() {
        RegisterRequest register = new RegisterRequest("username", "password", "email");
        userService.register(register);
        LoginRequest req = new LoginRequest("username", "password");
        LoginResult res = userService.login(req);
        AuthDAO authDAO = userService.getAuthDAO();
        AuthData authData = authDAO.verifyAuth(res.authToken());
        Assertions.assertNotNull(authData, "Token not registered in database.");
    }

    @Test
    @Order(4)
    public void LoginUserUnauthorized() {
        LoginRequest req = new LoginRequest("bad_username", "password");
        Assertions.assertThrows(UnauthorizedRequest.class, () -> userService.login(req), "Username wasn't flagged as unauthorized.");
    }

    @Test
    @Order(5)
    public void LogoutUserSuccess() {
        RegisterResult registerResult;
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        registerResult = userService.register(registerRequest);
        String authToken = registerResult.authToken();
        LogoutRequest req = new LogoutRequest(authToken);
        LogoutResult logoutResult = userService.logout(req);
        AuthDAO authDAO = userService.getAuthDAO();
        AuthData authData = authDAO.getAuth("username");
        Assertions.assertNull(authData, "AuthData not deleted from database.");
    }

    @Test
    @Order(6)
    public void LogoutUserUnauthorized() {
        LogoutRequest req = new LogoutRequest("bad_token");
        Assertions.assertThrows(UnauthorizedRequest.class, () -> userService.logout(req), "Username wasn't flagged as unauthorized.");
    }
}
