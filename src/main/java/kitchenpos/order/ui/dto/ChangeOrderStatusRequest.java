package kitchenpos.order.ui.dto;

import kitchenpos.order.domain.OrderStatus;

public class ChangeOrderStatusRequest {

    private OrderStatus orderStatus;

    private ChangeOrderStatusRequest() {
    }

    public ChangeOrderStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
