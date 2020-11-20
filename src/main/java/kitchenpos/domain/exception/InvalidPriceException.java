package kitchenpos.domain.exception;

public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException() {
        super("적절하지 않은 가격 값입니다");
    }
}
