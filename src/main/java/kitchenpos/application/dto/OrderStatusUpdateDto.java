package kitchenpos.application.dto;

import kitchenpos.domain.OrderStatus;

public class OrderStatusUpdateDto {

    private final OrderStatus orderStatus;

    public OrderStatusUpdateDto(final String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
