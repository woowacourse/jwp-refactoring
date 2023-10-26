package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

public class OrderCreatedEvent {
    private final Long id;
    private final Long orderTableId;

    public OrderCreatedEvent(Long id, Long orderTableId) {
        this.id = id;
        this.orderTableId = orderTableId;
    }

    public static OrderCreatedEvent from(Order order) {
        return new OrderCreatedEvent(order.getId(), order.getOrderTableId());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
