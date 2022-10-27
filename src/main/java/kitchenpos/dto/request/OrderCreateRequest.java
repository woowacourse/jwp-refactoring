package kitchenpos.dto.request;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final String orderStatus;
    private final List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final String orderStatus,
                              final List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public OrderCreateRequest(final Long orderTableId,
                              final List<OrderLineItemCreateRequest> orderLineItems) {
        this(orderTableId, OrderStatus.COOKING.name(), orderLineItems);
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
                orderStatus,
                LocalDateTime.now(),
                getOrderLineItems());
    }
}
