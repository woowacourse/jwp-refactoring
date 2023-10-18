package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.OrderStatus;

public class OrderUpdateRequest {

    private OrderStatus orderStatus;

    protected OrderUpdateRequest() {
    }

    public OrderUpdateRequest(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @JsonCreator
    public static OrderUpdateRequest from(final String orderStatus) {
        final String upperCase = orderStatus.toUpperCase();
        return new OrderUpdateRequest(OrderStatus.valueOf(upperCase));
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
