package kitchenpos.dto.order;

import kitchenpos.dto.orderlineitem.OrderLineItemCreateRequest;

import java.util.List;

public class OrderCreateRequest {
    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItemCreateRequests;

    public OrderCreateRequest() {
    }

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
