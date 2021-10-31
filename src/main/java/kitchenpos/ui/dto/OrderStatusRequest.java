package kitchenpos.ui.dto;

import kitchenpos.domain.Order;

public class OrderStatusRequest {
    private String orderStatus;

    private OrderStatusRequest() {
    }

    private OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatusRequest from(String orderStatus) {
        return new OrderStatusRequest(orderStatus);
    }

    public Order toOrder() {
        return new Order(null, orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
