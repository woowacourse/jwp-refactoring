package kitchenpos.dto.order;

import java.util.List;

public class CreateOrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    private CreateOrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public CreateOrderRequest() {
    }

    public static CreateOrderRequest of(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        return new CreateOrderRequest(orderTableId, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
