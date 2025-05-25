package service;

/**
 * Indicates a request has been taken by another user.
 */
public class AlreadyTaken extends RuntimeException {
    public AlreadyTaken() {
        super("Error: already taken");
    }
}
