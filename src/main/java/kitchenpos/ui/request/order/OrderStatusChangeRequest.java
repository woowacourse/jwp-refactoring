package kitchenpos.ui.request.order;

import kitchenpos.domain.OrderStatus;

public class OrderStatusChangeRequest {

    private OrderStatus orderStatus;

    public OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
