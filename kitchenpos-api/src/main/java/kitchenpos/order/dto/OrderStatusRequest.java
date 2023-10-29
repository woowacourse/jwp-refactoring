package kitchenpos.order.dto;

import kitchenpos.order.OrderStatus;

public class OrderStatusRequest {

    private final OrderStatus orderStatus;

    public OrderStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
