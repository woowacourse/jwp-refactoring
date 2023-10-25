package kitchenpos.application.dto;

import kitchenpos.domain.order.OrderStatus;

public class OrderChangeStatusRequest {

    private OrderStatus orderStatus;

    public OrderChangeStatusRequest() {
    }

    public OrderChangeStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
