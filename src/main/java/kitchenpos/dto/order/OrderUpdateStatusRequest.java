package kitchenpos.dto.order;

import kitchenpos.domain.OrderStatus;

public class OrderUpdateStatusRequest {

    private final OrderStatus orderStatus;

    private OrderUpdateStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderUpdateStatusRequest from(final OrderStatus orderStatus) {
        return new OrderUpdateStatusRequest(orderStatus);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
