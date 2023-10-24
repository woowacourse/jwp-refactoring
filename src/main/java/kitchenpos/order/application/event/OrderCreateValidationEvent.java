package kitchenpos.order.application.event;

public class OrderCreateValidationEvent {

    private final Long orderTableId;

    public OrderCreateValidationEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
