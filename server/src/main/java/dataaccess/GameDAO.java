package dataaccess;

import model.GameData;
import model.GameDataJson;
import java.util.HashSet;

public interface GameDAO {

    int generateGameID();

    GameData getGame(int gameID);

    GameData createGame(String gameName);

    HashSet<GameDataJson> listGames();

    void clearAllGames();
}
