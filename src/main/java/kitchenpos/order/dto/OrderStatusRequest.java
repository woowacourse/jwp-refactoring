package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderStatusRequest {

    private String orderStatus;

    @JsonCreator
    public OrderStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
