package dataaccess;

import model.GameData;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {

    private final HashSet<GameData> gameData = new HashSet<>();

    @Override
    public void clearAllGames() {
        gameData.clear();
    }

    @Override
    public String toString() {
        return "MemoryGameDAO{" +
                "gameData=" + gameData +
                '}';
    }
}
