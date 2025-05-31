package dataaccess.MySQLDAO;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;
import model.ListGameData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class MySQLGameDAO implements GameDAO {

    private final Random random = new Random();

    /**
     * Generates a new gameID.
     *
     * @return int gameID
     */
    @Override
    public int generateGameID() {
        return random.nextInt(10000);
    }

    /**
     * Gets the GameData with the given gameID.
     *
     * @param gameID gameID to search for
     * @return GameData with gameID; null if username isn't found
     */
    @Override
    public GameData getGame(int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            return queryGame(conn, gameID);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to connect to database.", ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to connect to server.", ex);
        }
    }

    private GameData queryGame(Connection conn, int gameID) {
        String query = "SELECT * FROM game WHERE gameID = ?";
        try (var preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, gameID);
            try (var rs = preparedStatement.executeQuery()) {
                return getGameData(rs);
            } catch (SQLException ex) {
                return null;
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    private GameData getGameData(ResultSet rs) throws SQLException {
        rs.next();
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String gameJson = rs.getString("game");
        var game = new Gson().fromJson(gameJson, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    /**
     * Creates a new GameData object with the given gameName.
     *
     * @param gameName gameName to use in the GameData
     * @return new GameData object
     */
    @Override
    public GameData createGame(String gameName) {
        try (var conn = DatabaseManager.getConnection()) {
            int gameID = generateGameID();
            ChessGame game = new ChessGame();
            insertGame(conn, gameID, gameName, game);
            return new GameData(gameID, null, null, gameName, game);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to connect to database.", ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void insertGame(Connection conn, int gameID, String gameName, ChessGame game)
            throws DataAccessException {
        var statement = "INSERT INTO game (gameID, gameName, game) VALUES(?, ?, ?)";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, gameName);
            var game_json = new Gson().toJson(game);
            preparedStatement.setString(3, game_json);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to add game to database.", ex);
        }
    }

    /**
     * Removes the given GameData object from the database.
     *
     * @param game GameData to remove
     */
    @Override
    public void deleteGame(GameData game) {

    }

    /**
     * Registers the given userName as the playerColor in the
     * GameData object.
     *
     * @param gameData The GameData to update
     * @param userName The userName to add
     * @param playerColor The player to register the user as
     */
    @Override
    public void updateGame(GameData gameData, String userName, String playerColor) {

    }

    /**
     * Lists all the GameData objects currently in the database.
     *
     * @return ArrayList of GameDataJson objects
     *         (GameData objects prepared to be added to a json file)
     */
    @Override
    public ArrayList<ListGameData> listGames() {
        return null;
    }

    /**
     * Removes all GameData objects from the database.
     */
    @Override
    public void clearAllGames() {
        try (var conn = DatabaseManager.getConnection()) {
            deleteAllUsers(conn);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to connect to database.", ex);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deleteAllUsers(Connection conn) throws DataAccessException {
        var statement = "DELETE FROM game";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to clear database.", ex);
        }
    }
}
