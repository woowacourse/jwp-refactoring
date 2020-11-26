package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;

import javax.validation.constraints.NotNull;

public class OrderStatusChangeRequest {

    @NotNull
    private OrderStatus orderStatus;

    public OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
