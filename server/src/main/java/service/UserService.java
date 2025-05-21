package service;

import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;

public class UserService {

    private static final UserDAO userDAO = new MemoryUserDAO();
    private static final AuthDAO authDAO = new MemoryAuthDAO();

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException {
        createUser(request);
        AuthData tokenData = createToken(request.username());
        return new RegisterResult(tokenData.userName(), tokenData.authToken(), null);
    }

    private void createUser(RegisterRequest request) throws AlreadyTakenException {
        if (userDAO.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Error: username already taken");
        }
        userDAO.createUser(request.username(), request.password(), request.email());
    }

    private AuthData createToken(String username) {
        return authDAO.createAuth(username);
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public AuthDAO getAuthDAO() {
        return authDAO;
    }
}
