package dataaccess;

import dataaccess.MySQLDAO.MySQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;

public class SQLUserDAOTests {

    private final UserDAO userDAO = new MySQLUserDAO();

    @BeforeAll
    public static void setUp() {
        Assertions.assertDoesNotThrow(DatabaseManager::configureDatabase, "Threw database error.");
    }

    @BeforeEach
    public void clear() {
        userDAO.clearAllUsers();
    }

    @Test
    @Order(1)
    public void createValidUser() {
        userDAO.createUser("username", "password", "email");
        UserData actual = userDAO.getUser("username");
        Assertions.assertNotNull(actual, "The user wasn't found in the database.");
    }

    @Test
    @Order(2)
    public void createInvalidUser() {
        userDAO.createUser("username", "password", "email");
        Assertions.assertThrows(RuntimeException.class,
                () -> userDAO.createUser("username", "password", "email"));
    }

    @Test
    @Order(3)
    public void getValidUser() {
        userDAO.createUser("username", "password", "email");
        UserData actual = userDAO.getUser("username");
        Assertions.assertNotNull(actual, "The user wasn't found in the database.");
    }

    @Test
    @Order(4)
    public void getInvalidUser() {
        userDAO.createUser("username", "password", "email");
        UserData user = userDAO.getUser("username1");
        Assertions.assertNull(user);
    }

    @Test
    @Order(5)
    public void clearUsers() {
        userDAO.createUser("username", "password", "email");
        userDAO.createUser("username1", "password1", "email1");
        userDAO.createUser("username2", "password2", "email2");
        Assertions.assertNotNull(userDAO.getUser("username"));
        userDAO.clearAllUsers();
        UserData user = userDAO.getUser("username");
        Assertions.assertNull(user);
    }
}
