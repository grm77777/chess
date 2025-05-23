package service;

import service.results.ClearResult;

public class ClearService extends Service {

    public ClearResult clear() {
        authDAO.clearAllAuths();
        userDAO.clearAllUsers();
        gameDAO.clearAllGames();
        return new ClearResult(null);
    }
}
