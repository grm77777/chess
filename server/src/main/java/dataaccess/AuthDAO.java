package dataaccess;

import model.AuthData;

public interface AuthDAO {
    String generateToken();

    AuthData getAuth(String username);

    AuthData verifyAuth(String authToken);

    AuthData createAuth(String username);

    void deleteAuth(AuthData auth);

    void clearAllAuths();
}
