package kitchenpos.exception;

public class OrderNotCompletionException extends IllegalArgumentException {

    public OrderNotCompletionException() {
        super("주문이 완료되지 않았습니다");
    }
}
