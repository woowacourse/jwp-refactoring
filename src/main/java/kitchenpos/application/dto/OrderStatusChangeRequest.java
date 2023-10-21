package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.order.OrderStatus;

public class OrderStatusChangeRequest {

    private final OrderStatus orderStatus;

    @JsonCreator
    public OrderStatusChangeRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
