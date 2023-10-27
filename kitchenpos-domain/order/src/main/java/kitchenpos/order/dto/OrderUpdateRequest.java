package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderUpdateRequest {

    private OrderStatus orderStatus;

    private OrderUpdateRequest() {
    }

    public OrderUpdateRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
