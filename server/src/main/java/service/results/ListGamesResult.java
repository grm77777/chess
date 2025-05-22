package service.results;

import model.GameDataJson;
import java.util.ArrayList;

public record ListGamesResult(ArrayList<GameDataJson> games, String message) {
}
