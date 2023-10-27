package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItems;

    public CreateOrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }
}
