package dataaccess;

import model.UserData;
import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {

    private ArrayList<UserData> userData = new ArrayList<>();

    public UserData getUser(String username) {
        for (UserData user : userData) {
            if (username.equals(user.username())) {
                return user;
            }
        }
        return null;
    }


}
