package service;

public class AlreadyTakenException extends RuntimeException {
    public AlreadyTakenException() {
        super("Error: already taken");
    }
}
