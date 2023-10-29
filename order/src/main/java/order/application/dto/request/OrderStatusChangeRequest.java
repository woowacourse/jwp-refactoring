package order.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderStatusChangeRequest {

    private final String orderStatus;

    @JsonCreator
    public OrderStatusChangeRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
