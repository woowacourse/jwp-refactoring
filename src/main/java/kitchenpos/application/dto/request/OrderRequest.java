package kitchenpos.application.dto.request;

import java.util.List;

public class OrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItemRequests;

    private OrderRequest() {
        this(null, null);
    }

    public OrderRequest(
            Long orderTableId,
            List<OrderLineItemRequest> orderLineItemRequests
    ) {
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
