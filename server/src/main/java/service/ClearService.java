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
        AUTH_DAO.clearAllAuths();
        GAME_DAO.clearAllGames();
        USER_DAO.clearAllUsers();
        return new ClearResult(null);
    }
}
