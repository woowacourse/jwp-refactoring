package kitchenpos.dto;

import java.util.List;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final List<OrderLineItemInOrderDto> orderLineItems;

    public OrderCreateRequest(
            final Long orderTableId,
            final List<OrderLineItemInOrderDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemInOrderDto> getOrderLineItems() {
        return orderLineItems;
    }
}
