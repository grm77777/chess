package dataaccess.MySQLDAO;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.DatabaseManager;
import java.sql.*;
import model.UserData;

public class MySQLUserDAO implements UserDAO {

    /**
     * Gets the UserData with the given username.
     *
     * @param username username to search for
     * @return UserData with username; null if username isn't found
     */
    @Override
    public UserData getUser(String username) {
        try {
            return findUser(username);
        } catch (NoFoundException ex) {
            return null;
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public UserData findUser(String queryUsername) throws DataAccessException, NoFoundException {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "SELECT * FROM user WHERE username = ?";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, queryUsername);
                try (var rs = preparedStatement.executeQuery()) {
                    rs.next();
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
                } catch (SQLException ex) {
                    throw new NoFoundException("Failed to query user in database.", ex);
                }
            } catch (SQLException ex) {
                throw new NoFoundException("Failed to query user in database.", ex);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to connect to database.", ex);
        }
    }

    /**
     * Creates a new UserData object with the given username.
     *
     * @param username username to use in the UserData
     */
    @Override
    public void createUser(String username, String password, String email) {
        try {
            insertUser(username, password, email);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void insertUser(String username, String password, String email)
            throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO user (username, password, email) VALUES(?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new DataAccessException("Failed to add user to database.", ex);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to connect to database.", ex);
        }
    }

    /**
     * Removes all UserData objects from the database.
     */
    @Override
    public void clearAllUsers() {
        try {
            deleteAllUsers();
        } catch (DataAccessException ignored) {}
    }

    public void deleteAllUsers() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM user";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new DataAccessException("Failed to clear database.", ex);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to connect to database.", ex);
        }
    }
}
