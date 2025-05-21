package dataaccess;

import model.AuthData;

public interface AuthDAO {
    String generateToken();

    AuthData verifyAuth(String username);

    AuthData createAuth(String username);
}
