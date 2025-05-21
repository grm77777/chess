package dataaccess;

import model.UserData;
import model.AuthData;

public interface UserDAO {
    UserData getUser(String username);

    void createUser(String username, String password, String email);
}
