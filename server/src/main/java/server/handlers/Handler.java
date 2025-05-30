package server.handlers;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

public class Handler {

    protected void congfigureDatabase() throws DataAccessException {
        DatabaseManager.configureDatabase();
    }
}
