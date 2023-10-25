package kitchenpos.domain.exception;

public class InvalidUpdateNumberOfGuestsException extends IllegalArgumentException {

    public InvalidUpdateNumberOfGuestsException(final String message) {
        super(message);
    }
}
