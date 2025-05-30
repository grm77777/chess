package dataaccess.MySQLDAO;

public class NoFoundException extends Exception {
    public NoFoundException(String message, Throwable ex) {
        super(message, ex);
    }
}
