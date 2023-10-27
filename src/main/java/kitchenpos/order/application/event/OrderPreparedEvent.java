package kitchenpos.order.application.event;

public class OrderPreparedEvent {

    private Long orderId;

    public OrderPreparedEvent(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
