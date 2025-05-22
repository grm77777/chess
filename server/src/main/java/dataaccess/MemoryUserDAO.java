package dataaccess;

import model.UserData;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {

    private final HashSet<UserData> userData = new HashSet<>();

    @Override
    public UserData getUser(String username) {
        for (UserData user : userData) {
            if (username.equals(user.username())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) {
        UserData user = new UserData(username, password, email);
        userData.add(user);
    }

    @Override
    public void clearAllUsers() {
        userData.clear();
    }

    @Override
    public String toString() {
        return "MemoryUserDAO{" +
                "userData=" + userData +
                '}';
    }
}
