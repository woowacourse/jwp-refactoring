package kitchenpos.order.dto;

import kitchenpos.order.OrderStatus;

public class OrderStatusRequest {

    private OrderStatus orderStatus;

    public OrderStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    private OrderStatusRequest() {
    }
}
