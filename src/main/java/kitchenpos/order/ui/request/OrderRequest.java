package kitchenpos.order.ui.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this(orderTableId, null, null, orderLineItems);
    }
    public OrderRequest(final List<OrderLineItemRequest> orderLineItems) {
        this(null, null, null, orderLineItems);
    }

    public OrderRequest(final Long orderTableId, final String orderStatus,
                        final LocalDateTime orderedTime,
                        final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }


    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<OrderLineItem> toOrderLineItem() {
        return this.orderLineItems.stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());
    }
}
