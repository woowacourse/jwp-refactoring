package kitchenpos.ui.dto.request;

import java.util.List;

public class OrderCreationRequest {

    private Long orderTableId;
    private List<OrderLineItemReeust> orderLineItems;

    private OrderCreationRequest() {}

    public OrderCreationRequest(final Long orderTableId,
                                final List<OrderLineItemReeust> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemReeust> getOrderLineItems() {
        return orderLineItems;
    }
}
