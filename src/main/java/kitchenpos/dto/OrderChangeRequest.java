package kitchenpos.dto;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public class OrderChangeRequest {

    private String orderStatus;

    public OrderChangeRequest() {
    }

    private OrderChangeRequest(String orderStatus){
        this.orderStatus = orderStatus;
    }

    public static OrderChangeRequest of(Order order) {
        OrderStatus orderStatus = order.getOrderStatus();
        
        return new OrderChangeRequest(orderStatus.name());
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
