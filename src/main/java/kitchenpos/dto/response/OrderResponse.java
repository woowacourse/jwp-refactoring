package kitchenpos.dto.response;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Orders;

import java.time.LocalDateTime;

public class OrderResponse {

    private final Long id;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;

    public OrderResponse(Long id, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static OrderResponse from(final Orders orders) {
        return new OrderResponse(
                orders.getId(),
                orders.getOrderStatus(),
                orders.getOrderedTime()
        );
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
