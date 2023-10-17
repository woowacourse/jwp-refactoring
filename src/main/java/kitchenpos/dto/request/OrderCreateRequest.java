package kitchenpos.dto.request;

import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {
    private final OrderStatus orderStatus;

    public OrderCreateRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
