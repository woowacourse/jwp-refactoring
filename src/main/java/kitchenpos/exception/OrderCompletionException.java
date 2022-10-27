package kitchenpos.exception;

public class OrderCompletionException extends IllegalArgumentException {

    public OrderCompletionException() {
        super("주문이 이미 완료된 상태입니다");
    }
}
