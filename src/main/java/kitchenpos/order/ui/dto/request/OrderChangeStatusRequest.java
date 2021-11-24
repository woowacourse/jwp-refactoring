package kitchenpos.order.ui.dto.request;

import kitchenpos.order.domain.OrderStatus;

public class OrderChangeStatusRequest {

    private OrderStatus orderStatus;

    private OrderChangeStatusRequest() {
    }

    public OrderChangeStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
