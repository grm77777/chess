package service;

import dataaccess.*;
import model.AuthData;

public class Service {

    protected static final AuthDAO authDAO = new MemoryAuthDAO();
    protected static final UserDAO  userDAO = new MemoryUserDAO();
    protected static final GameDAO gameDAO = new MemoryGameDAO();

    public AuthDAO getAuthDAO() {
        return authDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public GameDAO getGameDAO() {
        return gameDAO;
    }

}
