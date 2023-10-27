package kitchenpos.application.exception;

public class NotAllowedUngroupException extends IllegalArgumentException {

    public NotAllowedUngroupException(final String message) {
        super(message);
    }
}
