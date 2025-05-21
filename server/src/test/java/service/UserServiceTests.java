package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {

    UserService userService = new UserService();

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
        try {
            RegisterRequest req = new RegisterRequest("username", "password", "email");
            userService.register(req);
        } catch (AlreadyTakenException ignored) {}
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
}
