package kitchenpos.ui.dto.request;

import kitchenpos.domain.OrderStatus;

public class OrderChangeStatusRequest {

    private OrderStatus orderStatus;

    private OrderChangeStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
