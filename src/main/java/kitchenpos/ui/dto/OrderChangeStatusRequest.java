package kitchenpos.ui.dto;

import kitchenpos.domain.order.OrderStatus;

public class OrderChangeStatusRequest {

    private OrderStatus orderStatus;

    private OrderChangeStatusRequest() {
    }

    public OrderChangeStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
