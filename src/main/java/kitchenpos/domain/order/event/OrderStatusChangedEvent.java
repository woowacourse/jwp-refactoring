package kitchenpos.domain.order.event;

import jdk.jfr.Event;
import kitchenpos.domain.order.OrderStatus;

public class OrderStatusChangedEvent extends Event {

    private final Long orderTableId;
    private final OrderStatus orderStatus;

    public OrderStatusChangedEvent(final Long orderTableId, final OrderStatus orderStatus) {
        super();
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
