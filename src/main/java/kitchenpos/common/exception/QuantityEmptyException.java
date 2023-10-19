package kitchenpos.common.exception;

public class QuantityEmptyException extends RuntimeException {

    public QuantityEmptyException() {
        super("수량은 0개 이상이어야 합니다.");
    }
}
