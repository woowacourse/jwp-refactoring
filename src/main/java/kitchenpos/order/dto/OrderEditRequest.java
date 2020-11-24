package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class OrderEditRequest {

    private OrderStatus orderStatus;

    public OrderEditRequest() {
    }

    public OrderEditRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
