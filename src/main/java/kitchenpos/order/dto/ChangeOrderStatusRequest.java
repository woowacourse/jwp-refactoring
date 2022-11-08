package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class ChangeOrderStatusRequest {
    private OrderStatus orderStatus;

    public ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
