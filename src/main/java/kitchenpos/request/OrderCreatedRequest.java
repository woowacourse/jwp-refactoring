package kitchenpos.request;

import kitchenpos.order.Order;

public class OrderCreatedRequest {

    private final Long orderTableId;
    private final Order order;

    public OrderCreatedRequest(Long orderTableId, Order order) {
        this.orderTableId = orderTableId;
        this.order = order;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public Order getOrder() {
        return order;
    }
}
