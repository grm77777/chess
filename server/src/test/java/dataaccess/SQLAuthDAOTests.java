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
        Assertions.assertNotNull(actual, "The username wasn't found in the database.");
    }

    @Test
    @Order(2)
    public void createInvalidAuth() {
        Assertions.assertThrows(RuntimeException.class, () -> authDAO.createAuth("username"),
                "The username wasn't registered as invalid.");
    }

    @Test
    @Order(3)
    public void getValidAuth() {
        userDAO.createUser("username", "password", "email");
        authDAO.createAuth("username");
        AuthData actual = authDAO.getAuth("username");
        Assertions.assertNotNull(actual, "The username wasn't found in the database.");
    }

    @Test
    @Order(4)
    public void getInvalidAuth() {
        userDAO.createUser("username", "password", "email");
        authDAO.createAuth("username");
        AuthData auth = authDAO.getAuth("username1");
        Assertions.assertNull(auth, "The username was found in the database.");
    }

    @Test
    @Order(5)
    public void verifyValidAuth() {
        userDAO.createUser("username", "password", "email");
        AuthData auth = authDAO.createAuth("username");
        AuthData actual = authDAO.verifyAuth(auth.authToken());
        Assertions.assertNotNull(actual, "The token wasn't found in the database.");
    }

    @Test
    @Order(6)
    public void verifyInvalidAuth() {
        userDAO.createUser("username", "password", "email");
        authDAO.createAuth("username");
        AuthData actual = authDAO.verifyAuth("authToken");
        Assertions.assertNull(actual, "The token was found in the database.");
    }

    @Test
    @Order(7)
    public void removeValidAuth() {
        userDAO.createUser("username", "password", "email");
        AuthData auth = authDAO.createAuth("username");
        authDAO.deleteAuth(auth);
        AuthData test = authDAO.getAuth("username");
        Assertions.assertNull(test, "The auth was found in the database.");
    }

    @Test
    @Order(8)
    public void removeInvalidAuth() {
        AuthData auth = new AuthData("authToken", "");
        authDAO.deleteAuth(auth);
        AuthData test = authDAO.getAuth("username");
        Assertions.assertNull(test, "The auth was found in the database.");
    }
}
