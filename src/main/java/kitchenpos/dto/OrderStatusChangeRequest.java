package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;

public class OrderStatusChangeRequest {

    private OrderStatus orderStatus;

    public OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
