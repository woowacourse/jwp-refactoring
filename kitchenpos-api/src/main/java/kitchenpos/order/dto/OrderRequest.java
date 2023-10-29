package kitchenpos.order.dto;

import java.util.List;

public class OrderRequest {

    private final Long orderTableId;
    private final List<OrderLineRequest> orderLineRequests;

    public OrderRequest(final Long orderTableId, final List<OrderLineRequest> orderLineRequests) {
        this.orderTableId = orderTableId;
        this.orderLineRequests = orderLineRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineRequest> getOrderLineRequests() {
        return orderLineRequests;
    }
}
