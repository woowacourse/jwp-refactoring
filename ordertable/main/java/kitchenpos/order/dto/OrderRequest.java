package ordertable.main.java.kitchenpos.order.dto;

import java.util.List;
import kitchenpos.order.dto.OrderLineItemRequest;

public class OrderRequest {
    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
