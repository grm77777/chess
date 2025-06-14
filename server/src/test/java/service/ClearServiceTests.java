package service;

import dataaccess.*;
import model.GameData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClearServiceTests {

    @Test
    @Order(1)
    public void registerValidInput() {
        ClearService clearService = new ClearService();
        clearService.clear();
        AuthDAO authDAO = clearService.getAuthDAO();
        GameDAO gameDAO = clearService.getGameDAO();
        UserDAO userDAO = clearService.getUserDAO();
        userDAO.createUser("username", "password", "email");
        authDAO.createAuth("username");
        GameData game = gameDAO.createGame("gameName");
        clearService.clear();
        Assertions.assertNull(authDAO.getAuth("username"), "AuthData not emptied.");
        Assertions.assertNull(userDAO.getUser("username"), "UserData not emptied.");
        Assertions.assertNull(gameDAO.getGame(game.gameID()), "GameData not emptied.");
    }
}