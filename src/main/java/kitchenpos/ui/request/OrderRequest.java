package kitchenpos.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class OrderRequest {

    private final long orderTableId;
    private final List<OrderLineItemRequest> orderLineItems;

    @JsonCreator
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
