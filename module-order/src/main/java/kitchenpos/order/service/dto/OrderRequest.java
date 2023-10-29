package kitchenpos.order.service.dto;

import java.util.List;

public class OrderRequest {

    private long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(final long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
