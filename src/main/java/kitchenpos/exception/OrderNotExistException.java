package kitchenpos.exception;

public class OrderNotExistException extends RuntimeException {
    public OrderNotExistException() {
        super("주문이 존재하지 않습니다");
    }
}
