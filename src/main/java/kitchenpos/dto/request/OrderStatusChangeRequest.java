package kitchenpos.dto.request;

import kitchenpos.domain.OrderStatus;

public class OrderStatusChangeRequest {

    private final OrderStatus orderStatus;

    public OrderStatusChangeRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

}
