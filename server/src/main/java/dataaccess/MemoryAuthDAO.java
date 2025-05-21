package dataaccess;

import model.AuthData;
import java.util.UUID;
import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO {

    private HashSet<AuthData> authData = new HashSet<>();

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public AuthData createAuth(String username) {
        String token = generateToken();
        AuthData auth = new AuthData(token, username);
        authData.add(auth);
        return auth;
    }
}
