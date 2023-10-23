package kitchenpos.domain.exception;

public class InvalidQuantityException extends IllegalArgumentException {

    public InvalidQuantityException() {
        super("유효하지 않은 수량입니다.");
    }
}
