package kitchenpos.ui.dto;

import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineCreateRequest> orderLineCreateRequests;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineCreateRequest> orderLineCreateRequests) {
        this.orderTableId = orderTableId;
        this.orderLineCreateRequests = orderLineCreateRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineCreateRequest> getOrderLineCreateRequests() {
        return orderLineCreateRequests;
    }
}
