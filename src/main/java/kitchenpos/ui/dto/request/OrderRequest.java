package kitchenpos.ui.dto.request;

import java.util.List;

public class OrderRequest {

    private final Long orderTableId;
    private final List<OrderLineRequest> orderLineItemCreates;

    public OrderRequest(final Long orderTableId, final List<OrderLineRequest> orderLineItemCreates) {
        this.orderTableId = orderTableId;
        this.orderLineItemCreates = orderLineItemCreates;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineRequest> getOrderLineItemCreates() {
        return orderLineItemCreates;
    }
}
