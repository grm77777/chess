package dataaccess.MySQLDAO;

import dataaccess.AuthDAO;
import model.AuthData;

import java.util.UUID;

public class MySQLAuthDAO implements AuthDAO {

    /**
     * Generates a new authToken.
     *
     * @return String authToken
     */
    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Gets the AuthData with the given username.
     *
     * @param username Username to search for
     * @return AuthData with username; null if username isn't found
     */
    @Override
    public AuthData getAuth(String username) {
        return null;
    }

    /**
     * Verifies that a given authToken exists.
     *
     * @param authToken authToken to search for
     * @return AuthData with authToken; null if authToken isn't found
     */
    @Override
    public AuthData verifyAuth(String authToken) {
        return null;
    }

    /**
     * Creates a new AuthData object with the given username.
     *
     * @param username username to use in the AuthData
     * @return new AuthData object
     */
    @Override
    public AuthData createAuth(String username) {
        return null;
    }

    /**
     * Removes the given AuthData object from the database.
     *
     * @param auth AuthData to remove
     */
    @Override
    public void deleteAuth(AuthData auth) {

    }

    /**
     * Removes all AuthData objects from the database.
     */
    @Override
    public void clearAllAuths() {

    }
}
