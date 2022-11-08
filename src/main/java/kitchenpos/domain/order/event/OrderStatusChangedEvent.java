package kitchenpos.domain.order.event;

import jdk.jfr.Event;

public class OrderStatusChangedEvent extends Event {

    private final Long orderTableId;
    private final String orderStatus;

    public OrderStatusChangedEvent(final Long orderTableId, final String orderStatus) {
        super();
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
