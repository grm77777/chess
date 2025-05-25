package service;

/**
 * Indicates a request was incorrectly formatted
 * and/or contained bad data.
 */
public class BadRequest extends RuntimeException {
    public BadRequest() {
        super("Error: bad request");
    }
}
