package kitchenpos.dto.request;

import kitchenpos.domain.OrderStatus;

public class OrderStatusUpdateRequest {

    private OrderStatus orderStatus;

    private OrderStatusUpdateRequest() {
    }

    public OrderStatusUpdateRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
