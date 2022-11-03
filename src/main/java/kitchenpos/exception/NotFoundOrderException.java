package kitchenpos.exception;

public class NotFoundOrderException extends IllegalArgumentException {

    public NotFoundOrderException() {
        super("해당 주문이 존재하지 않습니다");
    }
}
