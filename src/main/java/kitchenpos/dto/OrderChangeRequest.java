package kitchenpos.dto;

import kitchenpos.domain.Order;

public class OrderChangeRequest {

    private String orderStatus;

    public OrderChangeRequest() {
    }

    private OrderChangeRequest(String orderStatus){
        this.orderStatus = orderStatus;
    }

    public static OrderChangeRequest of(Order order) {
        return new OrderChangeRequest(order.getOrderStatus());
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
