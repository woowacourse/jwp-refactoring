package kitchenpos.exception;

public class EmptyOrderTableException extends RuntimeException {
    public EmptyOrderTableException() {
        super("주문 테이블이 비어있는 상태입니다.");
    }
}
