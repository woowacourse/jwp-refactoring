package kitchenpos.ui.dto;

import kitchenpos.domain.OrderStatus;

public class OrderUpdateRequest {

    private final OrderStatus orderStatus;

    public OrderUpdateRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
