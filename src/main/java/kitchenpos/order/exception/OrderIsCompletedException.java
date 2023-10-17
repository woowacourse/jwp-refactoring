package kitchenpos.order.exception;

public class OrderIsCompletedException extends RuntimeException {

    private static final String MESSAGE = "주문이 완료 단계입니다.";

    public OrderIsCompletedException() {
        super(MESSAGE);
    }
}
