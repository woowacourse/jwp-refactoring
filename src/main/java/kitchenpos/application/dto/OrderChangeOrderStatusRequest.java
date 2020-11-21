package kitchenpos.application.dto;

import kitchenpos.domain.OrderStatus;

public class OrderChangeOrderStatusRequest {
    private OrderStatus orderStatus;

    private OrderChangeOrderStatusRequest() {
    }

    public OrderChangeOrderStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
