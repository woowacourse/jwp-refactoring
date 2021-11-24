package kitchenpos.exception.price;

public class InvalidePriceValueException extends RuntimeException {
    private static final String MESSAGE = "가격은 0보다 작거나 null일 수 없습니다.";

    public InvalidePriceValueException() {
        super(MESSAGE);
    }
}
