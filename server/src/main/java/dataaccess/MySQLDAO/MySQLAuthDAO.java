package dataaccess.MySQLDAO;

import dataaccess.AuthDAO;
import model.AuthData;

public class MySQLAuthDAO implements AuthDAO {

    @Override
    public String generateToken() {
        return "";
    }

    @Override
    public AuthData getAuth(String username) {
        return null;
    }

    @Override
    public AuthData verifyAuth(String authToken) {
        return null;
    }

    @Override
    public AuthData createAuth(String username) {
        return null;
    }

    @Override
    public void deleteAuth(AuthData auth) {

    }

    @Override
    public void clearAllAuths() {

    }
}
