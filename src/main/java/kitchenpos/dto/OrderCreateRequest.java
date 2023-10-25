package kitchenpos.dto;

import java.util.List;

public class OrderCreateRequest {

    private long orderTableId;
    private List<OrderCreateOrderLineItemRequest> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(final long orderTableId, final List<OrderCreateOrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderCreateOrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
