package kitchenpos.order.application.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<Long, Long> getMenuQuantityMap() {
        return orderLineItems.stream()
                .collect(Collectors.toMap(OrderLineItemRequest::getMenuId, OrderLineItemRequest::getMenuId));
    }
}
