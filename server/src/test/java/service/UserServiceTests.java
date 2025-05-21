package service;

import dataaccess.UserDAO;
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
        userService.register(req);
        Assertions.assertThrows(AlreadyTakenException.class, () -> userService.register(req), "Username wasn't registered as already taken.");
    }
}
