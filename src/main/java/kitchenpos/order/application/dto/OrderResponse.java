package kitchenpos.order.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems;

    private OrderResponse() {
    }

    public OrderResponse(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                         final LocalDateTime orderedTime,
                         final List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                toDtos(order.getOrderLineItems())
        );
    }

    private static List<OrderLineItemDto> toDtos(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemDto::of)
                .collect(Collectors.toList());
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

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
