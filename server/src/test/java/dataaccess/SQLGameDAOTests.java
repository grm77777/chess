package dataaccess;

import chess.ChessGame;
import dataaccess.MySQLDAO.MySQLGameDAO;
import dataaccess.MySQLDAO.MySQLUserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;

public class SQLGameDAOTests {

    UserDAO userDAO = new MySQLUserDAO();
    GameDAO gameDAO = new MySQLGameDAO();

    @BeforeAll
    public static void setUp() {
        Assertions.assertDoesNotThrow(DatabaseManager::configureDatabase, "Threw database error.");
    }

    @BeforeEach
    public void clear() {
        userDAO.clearAllUsers();
        gameDAO.clearAllGames();
    }

    @Test
    @Order(1)
    public void createValidGame() {
        GameData game = gameDAO.createGame("gameName");
        GameData actual = gameDAO.getGame(game.gameID());
        Assertions.assertNotNull(actual, "The game wasn't found in the database.");
    }

    @Test
    @Order(2)
    public void createInvalidGame() {
        GameData game = gameDAO.createGame("gameName");
        GameData actual = gameDAO.getGame(1234);
        Assertions.assertNull(actual, "The game was found in the database.");
    }

    @Test
    @Order(3)
    public void getValidGame() {
        GameData game = gameDAO.createGame("gameName");
        GameData actual = gameDAO.getGame(game.gameID());
        Assertions.assertNotNull(actual, "The game wasn't found in the database.");
    }

    @Test
    @Order(4)
    public void getInvalidGame() {
        GameData game = gameDAO.createGame("gameName");
        GameData actual = gameDAO.getGame(1234);
        Assertions.assertNull(actual, "The game was found in the database.");
    }

    @Test
    @Order(5)
    public void removeValidGame() {
        GameData game = gameDAO.createGame("gameName");
        gameDAO.deleteGame(game);
        GameData test = gameDAO.getGame(game.gameID());
        Assertions.assertNull(test, "The game was found in the database.");
    }

    @Test
    @Order(6)
    public void removeInvalidGame() {
        GameData game = new GameData(1234, null, null,
                "gameName", new ChessGame());
        Assertions.assertDoesNotThrow(() -> gameDAO.deleteGame(game));
    }

}
