package service;

import dataaccess.*;
import service.results.ClearResult;

public class ClearService {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public ClearService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public ClearResult clear() {
        authDAO.clearAllAuths();
        userDAO.clearAllUsers();
        gameDAO.clearAllGames();
        return new ClearResult(null);
    }
}
