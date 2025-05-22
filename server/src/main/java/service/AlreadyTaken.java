package service;

public class AlreadyTaken extends RuntimeException {
    public AlreadyTaken() {
        super("Error: already taken");
    }
}
