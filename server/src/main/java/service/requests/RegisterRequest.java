package service.requests;

/**
 * Represents the request associated with Register.
 */
public record RegisterRequest(String username, String password, String email) {}
