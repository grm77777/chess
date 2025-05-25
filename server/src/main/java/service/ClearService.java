package service;

import service.results.ClearResult;

/**
 * Implements services associated with the Clear handler.
 */
public class ClearService extends Service {

    /**
     * Clears the static DAOs associated with all service classes.
     *
     * @return empty ClearRequest
     */
    public ClearResult clear() {
        authDAO.clearAllAuths();
        userDAO.clearAllUsers();
        gameDAO.clearAllGames();
        return new ClearResult(null);
    }
}
