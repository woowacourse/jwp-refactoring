package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    private OrderRequest() {
    }

    public Order toOrder() {
        return new Order(orderTableId, null, toOrderLineItemRequest(orderLineItems));
    }

    private List<OrderLineItem> toOrderLineItemRequest(List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::toOrderLineItemRequest)
                .collect(Collectors.toList());
    }
}
