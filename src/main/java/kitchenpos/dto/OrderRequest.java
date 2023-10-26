package kitchenpos.dto;

import java.util.List;

public class OrderRequest {
    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
