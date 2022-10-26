package kitchenpos.exception;

public class InvalidPriceException extends IllegalArgumentException {

    private static final String MESSAGE = "가격은 0원 이상입니다.";

    public InvalidPriceException() {
        super(MESSAGE);
    }
}
