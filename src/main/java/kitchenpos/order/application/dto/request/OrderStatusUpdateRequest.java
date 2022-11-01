package kitchenpos.order.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderStatusUpdateRequest {

    private final String orderStatus;

    @JsonCreator
    public OrderStatusUpdateRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
