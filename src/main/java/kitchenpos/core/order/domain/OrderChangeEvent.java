package kitchenpos.core.order.domain;

public class OrderChangeEvent {

    private final Long orderId;
    private final Long orderTableId;
    private final String orderStatus;

    public OrderChangeEvent(final Long orderId, final Long orderTableId, final String orderStatus) {
        this.orderId = orderId;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
