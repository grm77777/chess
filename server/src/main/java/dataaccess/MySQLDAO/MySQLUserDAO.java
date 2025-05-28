package dataaccess.MySQLDAO;

import dataaccess.UserDAO;
import model.UserData;

public class MySQLUserDAO implements UserDAO {

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {

    }

    @Override
    public void clearAllUsers() {

    }
}
