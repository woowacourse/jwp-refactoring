package kitchenpos.ui.dto;

import kitchenpos.domain.Orders;

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

    public Orders toOrder() {
        return new Orders(null, orderStatus);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
