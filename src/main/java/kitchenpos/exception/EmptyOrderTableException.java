package kitchenpos.exception;

public class EmptyOrderTableException extends RuntimeException {
    public EmptyOrderTableException() {
        super("주문을 등록할 수 없는 테이블입니다!");
    }
}
