package kitchenpos.exceptions;

public class OrderTableEmptyException extends RuntimeException {

    public OrderTableEmptyException() {
        super("테이블이 비어있습니다.");
    }
}
