package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameDataJson;
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
    public HashSet<GameDataJson> listGames() {
        HashSet<GameDataJson> gameDataJsons = new HashSet<>();
        for (GameData game : gameData) {
            int gameID = game.gameID();
            String gameName = game.gameName();
            String whiteUsername;
            if (game.whiteUsername() != null) {
                whiteUsername = game.whiteUsername();
            } else {
                whiteUsername = "";
            }
            String blackUsername;
            if (game.blackUsername() != null) {
                blackUsername = game.blackUsername();
            } else {
                blackUsername = "";
            }
            GameDataJson gameJson = new GameDataJson(gameID, whiteUsername, blackUsername, gameName);
            gameDataJsons.add(gameJson);
        }
        return gameDataJsons;
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
