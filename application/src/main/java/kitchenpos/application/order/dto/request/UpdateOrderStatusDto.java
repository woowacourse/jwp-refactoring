package kitchenpos.application.order.dto.request;

import kitchenpos.core.domain.order.OrderStatus;

public class UpdateOrderStatusDto {

    private final Long orderId;
    private final OrderStatus orderStatus;

    public UpdateOrderStatusDto(Long orderId, String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
