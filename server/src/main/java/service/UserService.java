package service;

import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import model.UserData;

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

    public LoginResult login(LoginRequest request) throws UnauthorizedRequest {
        verifyUser(request);
        AuthData tokenData = createToken(request.username());
        return new LoginResult(tokenData.userName(), tokenData.authToken(), null);
    }

    private void verifyUser(LoginRequest request) {
        UserData user = userDAO.getUser(request.username());
        if (user == null || !user.password().equals(request.password())) {
            throw new UnauthorizedRequest("Error: unauthorized");
        }
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
