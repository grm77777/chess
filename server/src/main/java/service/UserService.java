package service;

import model.AuthData;
import model.UserData;
import service.requests.*;
import service.results.*;

/**
 * Implements services associated with the Login, Logout,
 * and Register handlers.
 */
public class UserService extends Service {

    /**
     * Registers a new user.
     *
     * @param request RegisterRequest with details about the new user.
     * @throws AlreadyTaken if the username is already taken.
     * @return RegisterResult with the user's authToken.
     */
    public RegisterResult register(RegisterRequest request) throws AlreadyTaken {
        createUser(request);
        AuthData tokenData = createToken(request.username());
        return new RegisterResult(tokenData.userName(), tokenData.authToken(), null);
    }

    /**
     * Creates a new user.
     *
     * @param request RegisterRequest with details about the new user.
     * @throws AlreadyTaken if the username is already taken.
     */
    private void createUser(RegisterRequest request) throws AlreadyTaken {
        if (userDAO.getUser(request.username()) != null) {
            throw new AlreadyTaken();
        }
        userDAO.createUser(request.username(), request.password(), request.email());
    }

    /**
     * Logs in an existing user.
     *
     * @param request LoginRequest with details about the user.
     * @throws UnauthorizedRequest if the username isn't in the database.
     * @return LoginResult with the user's authToken.
     */
    public LoginResult login(LoginRequest request) throws UnauthorizedRequest {
        verifyUser(request);
        AuthData tokenData = createToken(request.username());
        return new LoginResult(tokenData.userName(), tokenData.authToken(), null);
    }

    /**
     * Verifies that a user exists (by username).
     *
     * @param request LoginRequest with details about the user.
     * @throws UnauthorizedRequest if the username isn't in the database.
     */
    private void verifyUser(LoginRequest request) throws UnauthorizedRequest {
        UserData user = userDAO.getUser(request.username());
        if (user == null) {
            throw new UnauthorizedRequest();
        }
        boolean validLogin = userDAO.verifyUser(request.username(), request.password());
        if (!validLogin) {
            throw new UnauthorizedRequest();
        }
    }

    /**
     * Logs out an existing user.
     *
     * @param request LogoutRequest with details about the user.
     * @throws UnauthorizedRequest if the authToken isn't in the database.
     * @return empty LogoutResult
     */
    public LogoutResult logout(LogoutRequest request) throws UnauthorizedRequest {
        AuthData authData = verifyUser(request);
        authDAO.deleteAuth(authData);
        return new LogoutResult(null);
    }

    /**
     * Verifies that a user exists (by authToken).
     *
     * @param request LogoutRequest with details about the user.
     * @throws UnauthorizedRequest if the authToken isn't in the database.
     */
    private AuthData verifyUser(LogoutRequest request) throws UnauthorizedRequest {
        AuthData user = authDAO.verifyAuth(request.authToken());
        if (user == null) {
            throw new UnauthorizedRequest();
        }
        return user;
    }

    /**
     * Creates an AuthData with a new token and the given username.
     *
     * @param username username to use.
     * @return AuthData with the username and a new token.
     */
    private AuthData createToken(String username) {
        return authDAO.createAuth(username);
    }
}
