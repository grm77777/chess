package dataaccess.memory;

import dataaccess.UserDAO;
import model.UserData;
import java.util.HashSet;

/**
 * Creates a UserData data access object that stores all
 * UserData objects in RAM memory.
 */
public class MemoryUserDAO implements UserDAO {

    private final HashSet<UserData> userData = new HashSet<>();

    /**
     * Gets the UserData with the given username.
     *
     * @param username username to search for
     * @return UserData with username; null if username isn't found
     */
    @Override
    public UserData getUser(String username) {
        for (UserData user : userData) {
            if (username.equals(user.username())) {
                return user;
            }
        }
        return null;
    }

    /**
     * Creates a new UserData object with the given username.
     *
     * @param username username to use in the UserData
     */
    @Override
    public void createUser(String username, String password, String email) {
        UserData user = new UserData(username, password, email);
        userData.add(user);
    }

    @Override
    public boolean verifyUser(String username, String password) {
        UserData user = getUser(username);
        if (user == null) {
            return false;
        } else {
            return password.equals(user.password());
        }
    }

    /**
     * Removes all UserData objects from the database.
     */
    @Override
    public void clearAllUsers() {
        userData.clear();
    }
}
