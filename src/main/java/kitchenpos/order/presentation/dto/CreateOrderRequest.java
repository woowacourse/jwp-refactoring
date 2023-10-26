package kitchenpos.order.presentation.dto;

import java.util.List;

public class CreateOrderRequest {

    private Long orderTableId;

    private List<OrderLineItemRequest> orderLineItems;

    private CreateOrderRequest() {
    }

    public CreateOrderRequest(final Long orderTableId,
                              final List<OrderLineItemRequest> orderLineItems) {
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
