package model;

/**
 * Represents the data associated with each game,
 * formatted in a way to be listed out for the client.
 */
public record ListGameData(Integer gameID, String whiteUsername, String blackUsername, String gameName) {
}
