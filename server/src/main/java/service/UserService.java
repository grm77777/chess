package service;

import model.AuthData;
import model.UserData;
import service.requests.*;
import service.results.*;

public class UserService extends Service {

    public RegisterResult register(RegisterRequest request) throws AlreadyTaken {
        createUser(request);
        AuthData tokenData = createToken(request.username());
        return new RegisterResult(tokenData.userName(), tokenData.authToken(), null);
    }

    private void createUser(RegisterRequest request) throws AlreadyTaken {
        if (userDAO.getUser(request.username()) != null) {
            throw new AlreadyTaken();
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
            throw new UnauthorizedRequest();
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
            throw new UnauthorizedRequest();
        }
        return user;
    }

    private AuthData createToken(String username) {
        return authDAO.createAuth(username);
    }
}
