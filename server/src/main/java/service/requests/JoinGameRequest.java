package service.requests;

/**
 * Represents the request associated with JoinGame.
 */
public record JoinGameRequest(String playerColor, Integer gameID) {
}
