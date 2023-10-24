package kitchenpos.exception;

public class EmptyTableException extends RuntimeException{
    public EmptyTableException(String message) {
        super(message);
    }
}
