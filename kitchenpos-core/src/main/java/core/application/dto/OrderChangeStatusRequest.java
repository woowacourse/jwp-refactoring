package core.application.dto;

import core.domain.OrderStatus;

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
