package kitchenpos.domain.dto;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final List<Long> orderLineItemIds;
    private final LocalDateTime orderedTime;

    public OrderResponse(final Long id, final Long orderTableId, final String orderStatus, final List<Long> orderLineItemIds, final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemIds = orderLineItemIds;
        this.orderedTime = orderedTime;
    }

    public static OrderResponse from(final Order order) {
        final List<Long> orderLineItemIds = order.getOrderLineItems().getValues().stream()
                .map(OrderLineItem::getSeq)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderTable().getId(), order.getOrderStatus().name(),
                orderLineItemIds, order.getOrderedTime());
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

    public List<Long> getOrderLineItemIds() {
        return orderLineItemIds;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }
}
