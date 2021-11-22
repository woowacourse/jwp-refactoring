package kitchenpos.ui.dto.request;

import java.util.List;

public class OrderCreatedRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderCreatedRequest() {
    }

    public OrderCreatedRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
