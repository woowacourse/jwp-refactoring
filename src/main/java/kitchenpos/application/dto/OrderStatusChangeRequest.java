package kitchenpos.application.dto;

import kitchenpos.domain.OrderStatus;

public class OrderStatusChangeRequest {

    private OrderStatus orderStatus;

    private OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
