package kitchenpos.dto;

import kitchenpos.domain.Order;

public class OrderStatusUpdateRequest {

    private String orderStatus;

    private OrderStatusUpdateRequest() {
    }

    public Order toOrder() {
        final Order order = new Order();
        order.setOrderStatus(orderStatus);
        return order;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
