package kitchenpos.dto.order;

import kitchenpos.domain.OrderStatus;

public class OrderUpdateStatusRequest {

    private OrderStatus orderStatus;

    private OrderUpdateStatusRequest() {
    }

    public OrderUpdateStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
