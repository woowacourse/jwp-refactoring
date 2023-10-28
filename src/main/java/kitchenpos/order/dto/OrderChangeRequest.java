package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderChangeRequest {

    private OrderStatus orderStatus;

    public OrderChangeRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
