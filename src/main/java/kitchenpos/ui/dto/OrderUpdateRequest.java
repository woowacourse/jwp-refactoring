package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.OrderStatus;

public class OrderUpdateRequest {
    private OrderStatus orderStatus;

    @JsonCreator
    public OrderUpdateRequest(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
