package server.handlers;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

public class Handler {

    protected void createDatabase() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }
}
