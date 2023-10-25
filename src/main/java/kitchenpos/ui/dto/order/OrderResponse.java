package kitchenpos.ui.dto.order;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<Long> orderLineItemIds;

    public OrderResponse(
            final Long id,
            final Long orderTableId,
            final String orderStatus,
            final LocalDateTime orderedTime,
            final List<Long> orderLineItemIds
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemIds = orderLineItemIds;
    }

    public static OrderResponse from(final Order savedOrder) {
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderTable().getId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(),
                convertToIds(savedOrder.getOrderLineItems())
        );
    }

    private static List<Long> convertToIds(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                             .map(OrderLineItem::getSeq)
                             .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<Long> getOrderLineItemIds() {
        return orderLineItemIds;
    }
}
