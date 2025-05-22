package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.requests.*;
import service.results.*;

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

    private void verifyUser(LoginRequest request) throws UnauthorizedRequest {
        UserData user = userDAO.getUser(request.username());
        if (user == null || !user.password().equals(request.password())) {
            throw new UnauthorizedRequest("Error: unauthorized");
        }
    }

    public LogoutResult logout(LogoutRequest request) throws UnauthorizedRequest {
        AuthData authData = verifyUser(request);
        authDAO.deleteAuth(authData);
        return new LogoutResult(null);
    }

    private AuthData verifyUser(LogoutRequest request) throws UnauthorizedRequest {
        AuthData user = authDAO.verifyAuth(request.authToken());
        if (user == null) {
            throw new UnauthorizedRequest("Error: unauthorized");
        }
        return user;
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
