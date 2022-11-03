package kitchenpos.exceptions;

public class OrderNotCompletionException extends RuntimeException {

    public OrderNotCompletionException() {
        super("주문이 완료되지 않았습니다.");
    }
}
