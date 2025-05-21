package service;

public class UnauthorizedRequest extends RuntimeException {
    public UnauthorizedRequest(String message) {
        super(message);
    }
}
