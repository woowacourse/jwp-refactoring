package kitchenpos.application.dto;

import java.util.Arrays;

import kitchenpos.domain.OrderStatus;

public class OrderStatusChangeRequest {
    private String orderStatus;

    private OrderStatusChangeRequest() {
    }

    public OrderStatusChangeRequest(String orderStatus) {
        this.orderStatus = orderStatus;
        validate(this.orderStatus);
    }

    private void validate(String orderStatus) {
        Arrays.stream(OrderStatus.values())
                .filter(value -> value.name().equals(orderStatus))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
