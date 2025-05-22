package service;

public class UnauthorizedRequest extends RuntimeException {
    public UnauthorizedRequest() {
        super("Error: unauthorized");
    }
}
