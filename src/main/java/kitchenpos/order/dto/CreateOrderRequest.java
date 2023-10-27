package kitchenpos.order.dto;

import java.util.List;

public class CreateOrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItems;

    public CreateOrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
