package kitchenpos.order.ui.dto;

import kitchenpos.order.domain.Order;

public class OrderChangeStatusRequest {
    private String orderStatus;

    public OrderChangeStatusRequest() {
    }

    public OrderChangeStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Order toEntity() {
        return new Order(null, orderStatus, null, null);
    }
}
