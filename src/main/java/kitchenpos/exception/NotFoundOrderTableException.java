package kitchenpos.exception;

public class NotFoundOrderTableException extends IllegalArgumentException {

    public NotFoundOrderTableException() {
        super("해당 주문 테이블이 존재하지 않습니다");
    }
}
