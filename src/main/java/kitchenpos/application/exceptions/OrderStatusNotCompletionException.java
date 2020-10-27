package kitchenpos.application.exceptions;

public class OrderStatusNotCompletionException extends RuntimeException {
    public OrderStatusNotCompletionException() {
        super("완료 상태가 아닙니다.");
    }
}
