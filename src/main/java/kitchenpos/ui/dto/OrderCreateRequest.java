package kitchenpos.ui.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public List<Long> extractMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public Map<Long, Long> extractMenuIdWithQuantityMap() {
        return orderLineItems.stream()
                .collect(Collectors.toMap(
                        OrderLineItemRequest::getMenuId,
                        OrderLineItemRequest::getQuantity
                ));
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
