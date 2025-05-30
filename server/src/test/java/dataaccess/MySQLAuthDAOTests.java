package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MySQLAuthDAOTests {

    @Test
    public void setUp() {
        Assertions.assertDoesNotThrow(DatabaseManager::configureDatabase, "Threw database error.");
    }
}
