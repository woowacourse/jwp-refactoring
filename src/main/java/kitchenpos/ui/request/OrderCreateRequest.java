package kitchenpos.ui.request;

import java.util.List;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final List<OrderLineItemCreateRequest> orderLineItemCreateRequests;

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemCreateRequests = orderLineItemCreateRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItemCreateRequests() {
        return orderLineItemCreateRequests;
    }
}
