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
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
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
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
    }

    private void insertGame(Connection conn, int gameID, String gameName, ChessGame game)
            throws DataAccessException {
        var statement = "INSERT INTO game (gameID, gameName, game, whiteUsername, blackUsername) " +
                "VALUES(?, ?, ?, NULL, NULL)";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, gameName);
            var game_json = new Gson().toJson(game);
            preparedStatement.setString(3, game_json);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    /**
     * Removes the given GameData object from the database.
     *
     * @param game GameData to remove
     */
    @Override
    public void deleteGame(GameData game) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM game WHERE gameID = ?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, game.gameID());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new DataAccessException(ex.getMessage());
            }
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
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
        String statement;
        if (playerColor.equals("WHITE")) {
            statement = "UPDATE game SET whiteUsername=? WHERE gameID=?";
        } else {
            statement = "UPDATE game SET blackUsername=? WHERE gameID=?";
        }
        try (Connection conn = DatabaseManager.getConnection()) {
            addUser(conn, userName, gameData.gameID(), statement);
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
    }

    void addUser(Connection conn, String username, int gameID, String statement) throws SQLException {
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, gameID);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Lists all the GameData objects currently in the database.
     *
     * @return ArrayList of GameDataJson objects
     *         (GameData objects prepared to be added to a json file)
     */
    @Override
    public ArrayList<ListGameData> listGames() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT * FROM game";
            try (var preparedStatement = conn.prepareStatement(query)) {
                try (var rs = preparedStatement.executeQuery()) {
                    return listGameData(rs);
                } catch (SQLException ex) {
                    return null;
                }
            } catch (SQLException ex) {
                return null;
            }
        } catch (DataAccessException | SQLException ex) {
            throw new RuntimeException("ERROR: " + ex.getMessage());
        }
    }

    private ArrayList<ListGameData> listGameData(ResultSet rs) throws SQLException {
        ArrayList<ListGameData> gameDataJsons = new ArrayList<>();
        while (rs.next()) {
            int gameID = rs.getInt("gameID");
            String whiteUsername = rs.getString("whiteUsername");
            String blackUsername = rs.getString("blackUsername");
            String gameName = rs.getString("gameName");
            ListGameData gameJson = new ListGameData(gameID, whiteUsername, blackUsername, gameName);
            gameDataJsons.add(gameJson);
        }
        return gameDataJsons;
    }

    /**
     * Removes all GameData objects from the database.
     */
    @Override
    public void clearAllGames() {
        try (var conn = DatabaseManager.getConnection()) {
            deleteAllUsers(conn);
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage());
        }
    }

    public void deleteAllUsers(Connection conn) throws DataAccessException {
        var statement = "DELETE FROM game";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
