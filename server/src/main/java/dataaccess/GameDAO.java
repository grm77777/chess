package dataaccess;

import model.GameData;
import model.ListGameData;
import java.util.ArrayList;

/**
 * Interface for GameData data access objects.
 */
public interface GameDAO {

    /**
     * Generates a new gameID.
     *
     * @return int gameID
     */
    int generateGameID();

    /**
     * Gets the GameData with the given gameID.
     *
     * @param gameID gameID to search for
     * @return GameData with gameID; null if username isn't found
     */
    GameData getGame(int gameID);

    /**
     * Creates a new GameData object with the given gameName.
     *
     * @param gameName gameName to use in the GameData
     * @return new GameData object
     */
    GameData createGame(String gameName);

    /**
     * Removes the given GameData object from the database.
     *
     * @param game GameData to remove
     */
    void deleteGame(GameData game);

    /**
     * Registers the given userName as the playerColor in the
     * GameData object.
     *
     * @param gameData The GameData to update
     * @param userName The userName to add
     * @param playerColor The player to register the user as
     */
    void updateGame(GameData gameData, String userName, String playerColor);

    /**
     * Lists all the GameData objects currently in the database.
     *
     * @return ArrayList of GameDataJson objects
     *         (GameData objects prepared to be added to a json file)
     */
    ArrayList<ListGameData> listGames();

    /**
     * Removes all GameData objects from the database.
     */
    void clearAllGames();
}
