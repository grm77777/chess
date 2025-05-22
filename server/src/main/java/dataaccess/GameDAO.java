package dataaccess;

import model.GameData;

public interface GameDAO {

    int generateGameID();

    GameData getGame(int gameID);

    GameData createGame(String gameName);

    void clearAllGames();
}
