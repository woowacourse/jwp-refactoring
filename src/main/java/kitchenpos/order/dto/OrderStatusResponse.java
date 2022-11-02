package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;

public class OrderStatusResponse {

    private final String orderStatus;

    private OrderStatusResponse(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static OrderStatusResponse from(Order order){
        return new OrderStatusResponse(order.getOrderStatus().name());
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
