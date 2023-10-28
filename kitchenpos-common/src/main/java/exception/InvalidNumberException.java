package exception;

public class InvalidNumberException extends RuntimeException {

    public InvalidNumberException(final String message) {
        super(message);
    }
}
