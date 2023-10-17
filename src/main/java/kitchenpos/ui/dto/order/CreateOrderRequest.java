package kitchenpos.ui.dto.order;

import java.util.List;

public class CreateOrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItemDto> orderLineItems;

    public CreateOrderRequest(final Long orderTableId, final List<OrderLineItemDto> orderLineItems) {
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
