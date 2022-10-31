package kitchenpos.order.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderSaveRequest {

    private final Long orderTableId;
    private final List<OrderLineItemSaveRequest> orderLineItems;

    public OrderSaveRequest(Long orderTableId, List<OrderLineItemSaveRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItem> toOrderLineItemsEntities(Long orderId) {
        return orderLineItems.stream()
            .map(it -> it.toEntity(orderId))
            .collect(Collectors.toList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
