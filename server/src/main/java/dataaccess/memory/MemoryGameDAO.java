package dataaccess.memory;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;
import model.ListGameData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Creates a GameData data access object that stores all
 * GameData objects in RAM memory.
 */
public class MemoryGameDAO implements GameDAO {

    private final Random random = new Random();
    private final HashSet<GameData> gameData = new HashSet<>();

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
        for (GameData game : gameData) {
            if (gameID == game.gameID()) {
                return game;
            }
        }
        return null;
    }

    /**
     * Creates a new GameData object with the given gameName.
     *
     * @param gameName gameName to use in the GameData
     * @return new GameData object
     */
    @Override
    public GameData createGame(String gameName) {
        int gameID = generateGameID();
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(gameID, null, null, gameName, chessGame);
        gameData.add(game);
        return game;
    }

    /**
     * Removes the given GameData object from the database.
     *
     * @param game GameData to remove
     */
    @Override
    public void deleteGame(GameData game) {
        gameData.remove(game);
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
        GameData newGameData;
        if (playerColor.equals("WHITE")) {
            newGameData = new GameData(gameData.gameID(), userName, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else {
            newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), userName, gameData.gameName(), gameData.game());
        }
        deleteGame(gameData);
        this.gameData.add(newGameData);
    }

    /**
     * Lists all the GameData objects currently in the database.
     *
     * @return ArrayList of GameDataJson objects
     *         (GameData objects prepared to be added to a json file)
     */
    @Override
    public ArrayList<ListGameData> listGames() {
        ArrayList<ListGameData> gameDataJsons = new ArrayList<>();
        for (GameData game : gameData) {
            int gameID = game.gameID();
            String gameName = game.gameName();
            String whiteUsername = game.whiteUsername();
            String blackUsername = game.blackUsername();
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
        gameData.clear();
    }
}
