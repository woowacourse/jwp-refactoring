package kitchenpos.dto.order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    private Long orderTableId;

    private List<OrderLineItemRequest> orderLineItems;

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

    public List<Long> getMenuId() {
        return orderLineItems.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toUnmodifiableList());
    }
}
