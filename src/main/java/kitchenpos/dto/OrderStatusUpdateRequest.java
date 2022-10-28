package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;

public class OrderStatusUpdateRequest {

    private String orderStatus;

    private OrderStatusUpdateRequest() {
    }

    public OrderStatusUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
