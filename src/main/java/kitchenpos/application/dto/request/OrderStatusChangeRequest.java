package kitchenpos.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderStatusChangeRequest {

    private final String orderStatus;

    @JsonCreator
    public OrderStatusChangeRequest(@JsonProperty("orderStatus") String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}

