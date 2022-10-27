package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId,
        final List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.stream()
            .map(OrderLineItemCreateRequest::toEntity)
            .collect(Collectors.toList());
    }

    public Order toEntity() {
        return new Order(
            null,
            orderTableId,
            OrderStatus.COOKING.name(),
            LocalDateTime.now(),
            getOrderLineItems());
    }
}
