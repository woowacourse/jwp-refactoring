package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderStatus;

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
