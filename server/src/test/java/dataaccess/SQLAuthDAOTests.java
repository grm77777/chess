package dataaccess;

import dataaccess.MySQLDAO.MySQLAuthDAO;
import dataaccess.MySQLDAO.MySQLUserDAO;
import model.AuthData;
import org.junit.jupiter.api.*;

public class SQLAuthDAOTests {

    UserDAO userDAO = new MySQLUserDAO();
    AuthDAO authDAO = new MySQLAuthDAO();

    @BeforeAll
    public static void setUp() {
        Assertions.assertDoesNotThrow(DatabaseManager::configureDatabase, "Threw database error.");
    }

    @BeforeEach
    public void clear() {
        userDAO.clearAllUsers();
        authDAO.clearAllAuths();
    }

    @Test
    @Order(1)
    public void createValidAuth() {
        userDAO.createUser("username", "password", "email");
        authDAO.createAuth("username");
        AuthData actual = authDAO.getAuth("username");
        Assertions.assertNotNull(actual, "The token wasn't found in the database.");
    }

    @Test
    @Order(2)
    public void createInvalidAuth() {
        authDAO.createAuth("username");
        AuthData auth = authDAO.getAuth("username1");
        Assertions.assertNull(auth, "The token was found in the database.");
    }
}
