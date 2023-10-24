package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderStatus;

public class ChangeOrderStatusRequest {
    private final Long orderId;
    private final OrderStatus orderStatus;

    private ChangeOrderStatusRequest(final Long orderId, final OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public static ChangeOrderStatusRequest of(final Long orderId, final OrderStatus orderStatus) {
        return new ChangeOrderStatusRequest(orderId, orderStatus);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderId() {
        return orderId;
    }
}
