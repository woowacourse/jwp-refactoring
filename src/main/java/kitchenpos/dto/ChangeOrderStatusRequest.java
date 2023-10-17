package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;

public class ChangeOrderStatusRequest {
    private OrderStatus orderStatus;

    public ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
