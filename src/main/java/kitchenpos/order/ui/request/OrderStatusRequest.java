package kitchenpos.order.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderStatusRequest {

    private final String orderStatus;

    @JsonCreator
    public OrderStatusRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
