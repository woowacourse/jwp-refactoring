package order.dto;

import order.domain.OrderStatus;

public class ChangeOrderStatusRequest {
    private OrderStatus orderStatus;

    private ChangeOrderStatusRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ChangeOrderStatusRequest() {
    }

    public static ChangeOrderStatusRequest of(final OrderStatus orderStatus) {
        return new ChangeOrderStatusRequest(orderStatus);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

}
