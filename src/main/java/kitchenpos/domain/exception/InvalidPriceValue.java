package kitchenpos.domain.exception;

public class InvalidPriceValue extends IllegalArgumentException {

    public InvalidPriceValue(final String message) {
        super(message);
    }
}
