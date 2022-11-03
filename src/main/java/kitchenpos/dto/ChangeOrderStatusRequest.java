package kitchenpos.dto;

import kitchenpos.domain.order.OrderStatus;

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
