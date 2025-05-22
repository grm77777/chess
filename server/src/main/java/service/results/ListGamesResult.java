package service.results;

import model.GameDataJson;
import java.util.HashSet;

public record ListGamesResult(HashSet<GameDataJson> games, String message) {
}
