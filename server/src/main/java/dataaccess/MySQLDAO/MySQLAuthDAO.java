package dataaccess.MySQLDAO;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLAuthDAO implements AuthDAO {

    /**
     * Generates a new authToken.
     *
     * @return String authToken
     */
    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Gets the AuthData with the given username.
     *
     * @param username Username to search for
     * @return AuthData with username; null if username isn't found
     */
    @Override
    public AuthData getAuth(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            return queryAuth(conn, username, "SELECT * FROM auth WHERE username = ?");
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to connect to database.", ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to connect to server.", ex);
        }
    }

    private AuthData queryAuth(Connection conn, String username, String query) {
        try (var preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (var rs = preparedStatement.executeQuery()) {
                return getAuthData(rs);
            } catch (SQLException ex) {
                return null;
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    private AuthData getAuthData(ResultSet rs) throws SQLException {
        rs.next();
        String username = rs.getString("username");
        String authToken = rs.getString("authToken");
        return new AuthData(authToken, username);
    }

    /**
     * Verifies that a given authToken exists.
     *
     * @param authToken authToken to search for
     * @return AuthData with authToken; null if authToken isn't found
     */
    @Override
    public AuthData verifyAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            return queryAuth(conn, authToken, "SELECT * FROM auth WHERE authToken = ?");
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to connect to database.", ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to connect to server.", ex);
        }
    }

    /**
     * Creates a new AuthData object with the given username.
     *
     * @param username username to use in the AuthData
     * @return new AuthData object
     */
    @Override
    public AuthData createAuth(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            String authToken = generateToken();
            insertAuth(conn, authToken, username);
            return new AuthData(authToken, username);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to connect to database.", ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void insertAuth(Connection conn, String authToken, String username)
            throws DataAccessException {
        var statement = "INSERT INTO auth (username, authToken) VALUES(?, ?)";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to add user to database.", ex);
        }
    }

    /**
     * Removes the given AuthData object from the database.
     *
     * @param auth AuthData to remove
     */
    @Override
    public void deleteAuth(AuthData auth) {

    }

    /**
     * Removes all AuthData objects from the database.
     */
    @Override
    public void clearAllAuths() {
        try (var conn = DatabaseManager.getConnection()) {
            deleteAllAuths(conn);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to connect to database.", ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deleteAllAuths(Connection conn) throws DataAccessException {
        var statement = "DELETE FROM auth";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to clear database.", ex);
        }
    }
}
