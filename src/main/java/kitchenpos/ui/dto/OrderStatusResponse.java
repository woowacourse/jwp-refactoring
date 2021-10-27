package kitchenpos.ui.dto;

import kitchenpos.domain.Order;

public class OrderStatusResponse {
    private String orderStatus;

    public OrderStatusResponse() {
    }

    private OrderStatusResponse(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatusResponse from (Order order) {
        return new OrderStatusResponse(order.getOrderStatus());
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
