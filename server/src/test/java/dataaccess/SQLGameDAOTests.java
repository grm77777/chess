package dataaccess;

import chess.ChessGame;
import dataaccess.mySQLDAO.MySQLGameDAO;
import dataaccess.mySQLDAO.MySQLUserDAO;
import model.GameData;
import model.ListGameData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

public class SQLGameDAOTests {

    UserDAO userDAO = new MySQLUserDAO();
    GameDAO gameDAO = new MySQLGameDAO();

    @BeforeAll
    public static void setUp() {
        Assertions.assertDoesNotThrow(DatabaseManager::configureDatabase, "Threw database error.");
    }

    @BeforeEach
    public void clear() {
        gameDAO.clearAllGames();
        userDAO.clearAllUsers();
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

    @Test
    @Order(7)
    public void updatedValidGame() {
        GameData game = gameDAO.createGame("gameName");
        userDAO.createUser("username", "password", "email");
        gameDAO.updateGame(game, "username", "WHITE");
        GameData updatedGame = gameDAO.getGame(game.gameID());
        Assertions.assertEquals("username", updatedGame.whiteUsername(),
                "White username was not updated.");
    }

    @Test
    @Order(8)
    public void updatedInvalidGame() {
        GameData game = gameDAO.createGame("gameName");
        userDAO.createUser("username", "password", "email");
        Assertions.assertThrows(RuntimeException.class, () -> gameDAO.updateGame(game,
                "username1", "WHITE"), "Incorrect username was not caught.");
    }

    @Test
    @Order(9)
    public void listGamesCorrect() {
        GameData game0 = gameDAO.createGame("gameName");
        GameData game1 = gameDAO.createGame("gameName1");
        GameData game2 = gameDAO.createGame("gameName2");
        ArrayList<ListGameData> expected = new ArrayList<>();
        expected.add(new ListGameData(game0.gameID(), null, null,
                "gameName"));
        expected.add(new ListGameData(game1.gameID(), null, null,
                "gameName1"));
        expected.add(new ListGameData(game2.gameID(), null, null,
                "gameName2"));
        ArrayList<ListGameData> actual = gameDAO.listGames();
        Assertions.assertEquals(expected, actual, "Game list doesn't match expected.");
    }

    @Test
    @Order(10)
    public void listGamesIncorrect() {
        GameData game0 = gameDAO.createGame("gameName");
        GameData game1 = gameDAO.createGame("gameName1");
        GameData game2 = gameDAO.createGame("gameName2");
        ArrayList<ListGameData> expected = new ArrayList<>();
        expected.add(new ListGameData(game0.gameID(), null, null,
                "gameName"));
        expected.add(new ListGameData(game1.gameID(), null, null,
                "gameName1"));
        ArrayList<ListGameData> actual = gameDAO.listGames();
        Assertions.assertNotEquals(expected, actual, "Game list matches expected.");
    }

    @Test
    @Order(11)
    public void clearGames() {
        GameData game0 = gameDAO.createGame("gameName");
        GameData game1 = gameDAO.createGame("gameName1");
        GameData game2 = gameDAO.createGame("gameName2");
        Assertions.assertDoesNotThrow(() -> gameDAO.clearAllGames(), "Threw database error.");
    }

}
