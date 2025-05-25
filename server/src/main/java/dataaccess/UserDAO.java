package dataaccess;

import model.UserData;

/**
 * Interface for UserData data access objects.
 */
public interface UserDAO {

    /**
     * Gets the UserData with the given username.
     *
     * @param username username to search for
     * @return UserData with username; null if username isn't found
     */
    UserData getUser(String username);

    /**
     * Creates a new UserData object with the given username.
     *
     * @param username username to use in the UserData
     */
    void createUser(String username, String password, String email);

    /**
     * Removes all UserData objects from the database.
     */
    void clearAllUsers();
}
