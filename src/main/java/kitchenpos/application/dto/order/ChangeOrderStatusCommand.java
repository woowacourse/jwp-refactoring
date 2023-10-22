package kitchenpos.application.dto.order;

import kitchenpos.domain.OrderStatus;

public class ChangeOrderStatusCommand {

    private final Long orderId;
    private final OrderStatus orderStatus;

    public ChangeOrderStatusCommand(Long orderId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public Long orderId() {
        return orderId;
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }
}
