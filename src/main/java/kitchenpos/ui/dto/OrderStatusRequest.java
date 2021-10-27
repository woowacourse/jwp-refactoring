package kitchenpos.ui.dto;

import kitchenpos.domain.Order;

public class OrderStatusRequest {
    private String orderStatus;

    public OrderStatusRequest() {
    }

    private OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order toOrder() {
        return new Order(null, orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
