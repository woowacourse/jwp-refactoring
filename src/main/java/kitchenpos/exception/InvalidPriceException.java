package kitchenpos.exception;

public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException() {
        super("가격이 Null이거나 음수 일 수 없습니다.");
    }
}
