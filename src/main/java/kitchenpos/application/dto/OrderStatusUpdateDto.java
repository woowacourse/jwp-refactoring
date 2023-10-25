package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.order.OrderStatus;

public class OrderStatusUpdateDto {

    private final OrderStatus orderStatus;

    @JsonCreator
    public OrderStatusUpdateDto(final String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
