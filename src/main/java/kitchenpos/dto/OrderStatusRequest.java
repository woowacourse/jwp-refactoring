package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;

public class OrderStatusRequest {

    private String orderStatus;

    public OrderStatusRequest() {
    }

    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public OrderStatus toDomain() {
        return OrderStatus.valueOf(orderStatus);
    }
}
