package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrdersStatusRequest {

    private String orderStatus;

    @JsonCreator
    public OrdersStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
