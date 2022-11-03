package kitchenpos.order.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemDto> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItem> toOrderLineItems() {
        return orderLineItems.stream()
                .map(OrderLineItemDto::toEntity)
                .collect(Collectors.toList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
