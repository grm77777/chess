package service;

import dataaccess.*;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearServiceTests {

    @Test
    @Order(1)
    public void RegisterValidInput() {
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        ClearService clearService = new ClearService(authDAO, userDAO, gameDAO);
        authDAO.createAuth("username");
        userDAO.createUser("username", "password", "email");
        clearService.clear();
        Assertions.assertNull(authDAO.getAuth("username"), "AuthData not emptied.");
        Assertions.assertNull(userDAO.getUser("username"), "UserData not emptied.");
    }
}