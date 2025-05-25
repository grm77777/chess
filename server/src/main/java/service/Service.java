package service;

import dataaccess.*;

/**
 * Parent class of all services associated with the
 * different HTTP-request handlers.
 */
public class Service {

    protected static final AuthDAO authDAO = new MemoryAuthDAO();
    protected static final UserDAO  userDAO = new MemoryUserDAO();
    protected static final GameDAO gameDAO = new MemoryGameDAO();

    /**
     * Get that static AuthDAO associated with all service classes.
     *
     * @return authDAO
     */
    public AuthDAO getAuthDAO() {
        return authDAO;
    }

    /**
     * Get that static UserDAO associated with all service classes.
     *
     * @return userDAO
     */
    public UserDAO getUserDAO() {
        return userDAO;
    }

    /**
     * Get that static GameDAO associated with all service classes.
     *
     * @return gameDAO
     */
    public GameDAO getGameDAO() {
        return gameDAO;
    }

}
