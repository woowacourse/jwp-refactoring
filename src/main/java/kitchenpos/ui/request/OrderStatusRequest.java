package kitchenpos.ui.request;

import kitchenpos.domain.OrderStatus;

public class OrderStatusRequest {

    private OrderStatus orderStatus;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public static OrderStatusRequest create(OrderStatus orderStatus) {
        final OrderStatusRequest orderStatusRequest = new OrderStatusRequest();
        orderStatusRequest.orderStatus = orderStatus;
        return orderStatusRequest;
    }
}
