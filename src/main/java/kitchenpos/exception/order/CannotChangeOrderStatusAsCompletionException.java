package kitchenpos.exception.order;

public class CannotChangeOrderStatusAsCompletionException extends RuntimeException {
    private static final String MESSAGE = "이미 완료된 주문은 상태를 변경할 수 없습니다.";

    public CannotChangeOrderStatusAsCompletionException() {
        super(MESSAGE);
    }
}
