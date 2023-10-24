package kitchenpos.order.application.event;

public class OrderCreateEvent {

    private final Long orderTableId;

    public OrderCreateEvent(final Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
