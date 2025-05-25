package dataaccess;

import model.AuthData;

/**
 * Interface for AuthData data access objects.
 */
public interface AuthDAO {

    /**
     * Generates a new authToken.
     *
     * @return String authToken
     */
    String generateToken();

    /**
     * Gets the AuthData with the given username.
     *
     * @param username Username to search for
     * @return AuthData with username; null if username isn't found
     */
    AuthData getAuth(String username);

    /**
     * Verifies that a given authToken exists.
     *
     * @param authToken authToken to search for
     * @return AuthData with authToken; null if authToken isn't found
     */
    AuthData verifyAuth(String authToken);

    /**
     * Creates a new AuthData object with the given username.
     *
     * @param username username to use in the AuthData
     * @return new AuthData object
     */
    AuthData createAuth(String username);

    /**
     * Removes the given AuthData object from the database.
     *
     * @param auth AuthData to remove
     */
    void deleteAuth(AuthData auth);

    /**
     * Removes all AuthData objects from the database.
     */
    void clearAllAuths();
}
