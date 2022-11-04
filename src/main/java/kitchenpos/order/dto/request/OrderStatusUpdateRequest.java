package kitchenpos.order.dto.request;

import kitchenpos.order.domain.OrderStatus;

public class OrderStatusUpdateRequest {

    private OrderStatus orderStatus;

    private OrderStatusUpdateRequest() {
    }

    public OrderStatusUpdateRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
