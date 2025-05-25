package service.results;

/**
 * Represents the result associated with Login.
 */
public record LoginResult(String username, String authToken, String message) {
}
