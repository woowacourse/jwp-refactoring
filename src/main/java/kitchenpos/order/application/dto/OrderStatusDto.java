package kitchenpos.order.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderStatusDto {

    final String orderStatus;

    public OrderStatusDto(@JsonProperty("orderStatus") final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
