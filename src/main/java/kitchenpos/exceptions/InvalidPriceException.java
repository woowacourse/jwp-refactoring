package kitchenpos.exceptions;

public class InvalidPriceException extends RuntimeException {

    public InvalidPriceException() {
        super("유효한 가격이 아닙니다.");
    }
}
