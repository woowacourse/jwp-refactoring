package kitchenpos.application.dto.request;

import kitchenpos.application.dto.OrderLineItemDto;

import java.util.List;

public class OrderCreateRequest {

    final Long orderTableId;
    final List<OrderLineItemDto> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
