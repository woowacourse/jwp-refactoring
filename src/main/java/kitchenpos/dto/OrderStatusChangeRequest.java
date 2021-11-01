package kitchenpos.dto;

import kitchenpos.domain.Order;

public class OrderStatusChangeRequest {

    private String orderStatus;

    protected OrderStatusChangeRequest() {
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public Order toEntity() {
        return new Order(orderStatus);
    }
}
