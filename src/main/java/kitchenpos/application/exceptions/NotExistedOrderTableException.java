package kitchenpos.application.exceptions;

public class NotExistedOrderTableException extends RuntimeException {
    public NotExistedOrderTableException() {
        super("존재하지 않는 OrderTable입니다.");
    }
}
