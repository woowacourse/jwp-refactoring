package kitchenpos.dto.request;

import kitchenpos.domain.OrderStatus;

public class OrderStatusRequest {
    private OrderStatus orderStatus;

    public OrderStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
