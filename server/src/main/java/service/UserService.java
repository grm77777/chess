package service;

import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;

public class UserService {

    public RegisterResult register(RegisterRequest request) {
        System.out.println(request);

        UserDAO userDao = new MemoryUserDAO();
        UserData userData = userDao.getUser(request.username());
        System.out.println(userData);

        return new RegisterResult("username", "authToken", "message");
    }
}
