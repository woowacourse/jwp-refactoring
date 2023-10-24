package kitchenpos.order.application.dto;

import java.time.LocalDateTime;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.domain.Order;

public class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final OrderLineItemsResponse orderLineItems;

    private OrderResponse(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                          final LocalDateTime orderedTime,
                          final OrderLineItemsResponse orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
                order.getOrderedTime(), OrderLineItemsResponse.from(order.getOrderLineItems()));
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItemsResponse getOrderLineItems() {
        return orderLineItems;
    }
}
