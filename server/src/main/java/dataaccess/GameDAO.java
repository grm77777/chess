package dataaccess;

import model.GameData;
import model.GameDataJson;
import java.util.HashSet;

public interface GameDAO {

    int generateGameID();

    GameData getGame(int gameID);

    GameData createGame(String gameName);

    void deleteGame(GameData game);

    void updateGame(GameData gameData, String userName, String playerColor);

    HashSet<GameDataJson> listGames();

    void clearAllGames();
}
