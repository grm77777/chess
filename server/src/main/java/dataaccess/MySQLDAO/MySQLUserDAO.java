package dataaccess.MySQLDAO;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.DatabaseManager;
import java.sql.*;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class MySQLUserDAO implements UserDAO {

    /**
     * Gets the UserData with the given username.
     *
     * @param username username to search for
     * @return UserData with username; null if username isn't found
     */
    @Override
    public UserData getUser(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            return queryUser(conn, username);
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
    }

    private UserData queryUser(Connection conn, String username) {
        var query = "SELECT * FROM user WHERE username = ?";
        try (var preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (var rs = preparedStatement.executeQuery()) {
                return getUserData(rs);
            } catch (SQLException ex) {
                return null;
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    private UserData getUserData(ResultSet rs) throws SQLException {
        rs.next();
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    /**
     * Creates a new UserData object with the given username.
     *
     * @param username username to use in the UserData
     */
    @Override
    public void createUser(String username, String password, String email) {
        try (var conn = DatabaseManager.getConnection()) {
            insertUser(conn, username, password, email);
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
    }

    private void insertUser(Connection conn, String username, String password, String email)
            throws DataAccessException {
        String hashedPassword = hashPassword(password);
        var statement = "INSERT INTO user (username, password, email) VALUES(?, ?, ?)";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private String hashPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    @Override
    public boolean verifyUser(String username, String providedClearTextPassword) {
        var hashedPassword = readHashedPasswordFromDatabase(username);
        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }

    private String readHashedPasswordFromDatabase(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            UserData user = queryUser(conn, username);
            if (user != null) {
                return user.password();
            } else {
                return "";
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
    }

    /**
     * Removes all UserData objects from the database.
     */
    @Override
    public void clearAllUsers() {
        try (var conn = DatabaseManager.getConnection()) {
            deleteAllUsers(conn);
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
    }

    public void deleteAllUsers(Connection conn) throws DataAccessException {
        var statement = "DELETE FROM user";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
