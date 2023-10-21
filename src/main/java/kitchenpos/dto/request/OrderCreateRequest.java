package kitchenpos.dto.request;

import java.util.List;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final List<OrderLineItemsCreateRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemsCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemsCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
