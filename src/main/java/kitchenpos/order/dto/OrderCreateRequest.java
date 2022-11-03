package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderCreateRequest {
    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.stream()
                .map(it -> new OrderLineItem(null, null, it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
