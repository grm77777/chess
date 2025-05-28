package dataaccess.MySQLDAO;

import dataaccess.GameDAO;
import model.GameData;
import model.ListGameData;
import java.util.ArrayList;

public class MySQLGameDAO implements GameDAO {

    @Override
    public int generateGameID() {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public GameData createGame(String gameName) {
        return null;
    }

    @Override
    public void deleteGame(GameData game) {

    }

    @Override
    public void updateGame(GameData gameData, String userName, String playerColor) {

    }

    @Override
    public ArrayList<ListGameData> listGames() {
        return null;
    }

    @Override
    public void clearAllGames() {

    }
}
