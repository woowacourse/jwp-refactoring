package kitchenpos.dto.order;

import kitchenpos.domain.OrderStatus;

public class OrderUpdateStatusRequest {

    private final OrderStatus orderStatus;

    public OrderUpdateStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
