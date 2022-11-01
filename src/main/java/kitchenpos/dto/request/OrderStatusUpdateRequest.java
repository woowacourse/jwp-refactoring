package kitchenpos.dto.request;

import kitchenpos.domain.order.OrderStatus;

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
