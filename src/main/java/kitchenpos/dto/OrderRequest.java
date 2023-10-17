package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderTable;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }

    public Order toOrder(final OrderTable table) {
        final List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
                .map(orderLineItemRequest -> new OrderLineItem(
                        orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
        return new Order(table, new OrderLineItems(orderLineItems));
    }
}
