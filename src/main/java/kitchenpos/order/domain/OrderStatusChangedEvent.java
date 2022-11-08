package kitchenpos.order.domain;

import kitchenpos.core.event.Event;

public class OrderStatusChangedEvent extends Event {

    private final Long orderTableId;
    private final OrderStatus orderStatus;

    public OrderStatusChangedEvent(final Long orderTableId, final OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
