package kitchenpos.common.event;

public class ValidateOrdersCompletedEvent {

    private final Long orderTableId;

    public ValidateOrdersCompletedEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
