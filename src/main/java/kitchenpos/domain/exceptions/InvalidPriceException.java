package kitchenpos.domain.exceptions;

public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException() {
        super("가격의 범위가 올바르지 않습니다.");
    }
}
