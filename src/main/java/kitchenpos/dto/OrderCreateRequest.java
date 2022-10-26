package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public Order toOrder() {
        final List<OrderLineItem> orderLineItems = this.orderLineItems.stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());
        return new Order(null, orderTableId, null, null, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
