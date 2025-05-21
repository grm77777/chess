package service;

import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;

public class UserService {

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException {
        createUser(request);
        AuthData tokenData = createToken(request.username());
        return new RegisterResult(tokenData.userName(), tokenData.authToken(), null);
    }

    private void createUser(RegisterRequest request) throws AlreadyTakenException {
        UserDAO userDao = new MemoryUserDAO();
        if (userDao.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Error: username already taken");
        }
        userDao.createUser(request.username(), request.password(), request.email());
    }

    private AuthData createToken(String username) {
        AuthDAO authDAO = new MemoryAuthDAO();
        return authDAO.createAuth(username);
    }
}
