package service;

import dataaccess.*;
import dataaccess.mysql.MySQLAuthDAO;
import dataaccess.mysql.MySQLGameDAO;
import dataaccess.mysql.MySQLUserDAO;

/**
 * Parent class of all services associated with the
 * different HTTP-request handlers.
 */
public class Service {

    protected static final AuthDAO AUTH_DAO = new MySQLAuthDAO();
    protected static final UserDAO USER_DAO = new MySQLUserDAO();
    protected static final GameDAO GAME_DAO = new MySQLGameDAO();

    /**
     * Get that static AuthDAO associated with all service classes.
     *
     * @return AUTH_DAO
     */
    public AuthDAO getAuthDAO() {
        return AUTH_DAO;
    }

    /**
     * Get that static UserDAO associated with all service classes.
     *
     * @return USER_DAO
     */
    public UserDAO getUserDAO() {
        return USER_DAO;
    }

    /**
     * Get that static GameDAO associated with all service classes.
     *
     * @return GAME_DAO
     */
    public GameDAO getGameDAO() {
        return GAME_DAO;
    }

}
