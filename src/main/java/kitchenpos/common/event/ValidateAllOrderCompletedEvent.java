package kitchenpos.common.event;

public class ValidateAllOrderCompletedEvent {

    private final Long orderTableId;

    public ValidateAllOrderCompletedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
