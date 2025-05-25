package service.results;

import model.ListGameData;
import java.util.ArrayList;

/**
 * Represents the result associated with ListGames.
 */
public record ListGamesResult(ArrayList<ListGameData> games, String message) {
}
