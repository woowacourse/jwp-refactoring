package kitchenpos.order.application.event;

public class OrderPreparedEvent {

    private final Long orderId;

    public OrderPreparedEvent(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
