package kitchenpos.exception;

public class InvalidPriceValue extends IllegalArgumentException {

    public InvalidPriceValue(final String message) {
        super(message);
    }
}
