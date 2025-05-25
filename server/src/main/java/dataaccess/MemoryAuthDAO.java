package dataaccess;

import model.AuthData;
import java.util.UUID;
import java.util.HashSet;

/**
 * Creates a AuthData data access object that stores all
 * AuthData objects in RAM memory.
 */
public class MemoryAuthDAO implements AuthDAO {

    private final HashSet<AuthData> authData = new HashSet<>();

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
        for (AuthData auth : authData) {
            if (username.equals(auth.userName())) {
                return auth;
            }
        }
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
        for (AuthData auth : authData) {
            if (authToken.equals(auth.authToken())) {
                return auth;
            }
        }
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
        String token = generateToken();
        AuthData auth = new AuthData(token, username);
        authData.add(auth);
        return auth;
    }

    /**
     * Removes the given AuthData object from the database.
     *
     * @param auth AuthData to remove
     */
    @Override
    public void deleteAuth(AuthData auth) {
        authData.remove(auth);
    }

    /**
     * Removes all AuthData objects from the database.
     */
    @Override
    public void clearAllAuths() {
        authData.clear();
    }

}
