package kitchenpos.application.dto;

import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    protected OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItemRequests) {
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
