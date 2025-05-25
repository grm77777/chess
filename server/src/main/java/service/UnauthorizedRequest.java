package service;

/**
 * Indicates a request wasn't properly authorized.
 */
public class UnauthorizedRequest extends RuntimeException {
    public UnauthorizedRequest() {
        super("Error: unauthorized");
    }
}
