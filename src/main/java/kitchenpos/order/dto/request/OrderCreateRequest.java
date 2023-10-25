package kitchenpos.order.dto.request;

import java.util.List;

public class OrderCreateRequest {
    private final long orderTableId;
    private final List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest(final long orderTableId, final List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public long orderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> orderLineItems() {
        return orderLineItems;
    }
}
