package kitchenpos.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemDto> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId,
                              final List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder(final Long orderTableId,
                         final OrderStatus orderStatus,
                         final LocalDateTime orderedTime) {
        return new Order(
                orderTableId,
                orderStatus,
                orderedTime,
                toOrderLineItems()
        );
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }

    private List<OrderLineItem> toOrderLineItems() {
        return this.orderLineItems.stream()
                .map(OrderLineItemDto::toOrderLineItem)
                .collect(Collectors.toList());
    }
}
