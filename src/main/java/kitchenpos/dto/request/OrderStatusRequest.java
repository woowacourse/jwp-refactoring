package kitchenpos.dto.request;

import kitchenpos.domain.order.OrderStatus;

public class OrderStatusRequest {
    private final OrderStatus orderStatus;

    public OrderStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
