package dataaccess;

import model.AuthData;

import java.util.UUID;
import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO {

    private final HashSet<AuthData> authData = new HashSet<>();

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public AuthData getAuth(String username) {
        for (AuthData auth : authData) {
            if (username.equals(auth.userName())) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public AuthData verifyAuth(String authToken) {
        for (AuthData auth : authData) {
            if (authToken.equals(auth.authToken())) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public AuthData createAuth(String username) {
        String token = generateToken();
        AuthData auth = new AuthData(token, username);
        authData.add(auth);
        return auth;
    }

    @Override
    public void deleteAuth(AuthData auth) {
        authData.remove(auth);
    }

    @Override
    public void clearAllAuths() {
        authData.clear();
    }

    @Override
    public String toString() {
        return "MemoryAuthDAO{" +
                "authData=" + authData +
                '}';
    }
}
