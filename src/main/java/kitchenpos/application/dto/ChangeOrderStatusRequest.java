package kitchenpos.application.dto;

import kitchenpos.domain.OrderStatus;

public class ChangeOrderStatusRequest {

    private OrderStatus orderStatus;

    public ChangeOrderStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ChangeOrderStatusRequest() {
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
