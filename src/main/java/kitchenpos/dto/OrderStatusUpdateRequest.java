package kitchenpos.dto;

import kitchenpos.domain.Order;

public class OrderStatusUpdateRequest {

    private String orderStatus;

    private OrderStatusUpdateRequest() {
    }

    public Order toOrder() {
        return new Order(null, null, orderStatus, null, null);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
