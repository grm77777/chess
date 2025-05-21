package dataaccess;

import model.AuthData;

public interface AuthDAO {
    String generateToken();

    AuthData createAuth(String username);
}
