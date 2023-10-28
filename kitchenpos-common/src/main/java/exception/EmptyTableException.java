package exception;

public class EmptyTableException extends RuntimeException {

    public EmptyTableException(final String message) {
        super(message);
    }
}
