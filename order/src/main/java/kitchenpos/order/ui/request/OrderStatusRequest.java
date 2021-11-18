package kitchenpos.order.ui.request;

import kitchenpos.order.domain.OrderStatus;

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
