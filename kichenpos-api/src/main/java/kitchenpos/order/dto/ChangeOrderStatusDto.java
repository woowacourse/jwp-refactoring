package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderStatus;

public class ChangeOrderStatusDto {

    private final OrderStatus orderStatus;

    public ChangeOrderStatusDto(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
