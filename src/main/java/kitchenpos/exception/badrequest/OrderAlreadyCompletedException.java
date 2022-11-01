package kitchenpos.exception.badrequest;

public class OrderAlreadyCompletedException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "해당 주문은 이미 결제 완료되어 상태를 변경할 수 없습니다";
    private static final String MESSAGE_FORMAT = "해당 주문은 이미 결제 완료되어 상태를 변경할 수 없습니다 : %s";

    public OrderAlreadyCompletedException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderAlreadyCompletedException(final Long completedOrderId) {
        super(String.format(MESSAGE_FORMAT, completedOrderId));
    }
}
