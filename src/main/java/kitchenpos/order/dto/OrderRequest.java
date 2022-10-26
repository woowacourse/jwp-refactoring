package kitchenpos.order.dto;

import static java.util.stream.Collectors.*;

import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest() {
    }

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
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
            .collect(toList());
    }

    public long getQuantity(Long menuId) {
        return orderLineItems.stream()
            .collect(toMap(OrderLineItemRequest::getMenuId, OrderLineItemRequest::getQuantity))
            .getOrDefault(menuId, 0);
    }
}
