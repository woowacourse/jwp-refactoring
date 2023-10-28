package kitchenpos.exception;

public class NotFoundMenuGroupException extends IllegalArgumentException {

    public NotFoundMenuGroupException(final String message) {
        super(message);
    }
}
