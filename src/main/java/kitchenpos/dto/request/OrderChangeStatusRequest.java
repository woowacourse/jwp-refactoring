package kitchenpos.dto.request;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.domain.OrderStatus;

public class OrderChangeStatusRequest {
    private final String orderStatus;

    @JsonCreator
    public OrderChangeStatusRequest(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderChangeStatusRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus.name();
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
