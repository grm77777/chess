package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.HashSet;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {

    private final Random random = new Random();
    private final HashSet<GameData> gameData = new HashSet<>();

    @Override
    public int generateGameID() {
        return random.nextInt(10000);
    }

    @Override
    public GameData getGame(int gameID) {
        for (GameData game : gameData) {
            if (gameID == game.gameID()) {
                return game;
            }
        }
        return null;
    }

    @Override
    public GameData createGame(String gameName) {
        int gameID = generateGameID();
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(gameID, null, null, gameName, chessGame);
        gameData.add(game);
        return game;
    }

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
