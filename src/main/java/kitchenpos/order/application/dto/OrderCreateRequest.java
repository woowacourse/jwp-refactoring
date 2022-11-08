package kitchenpos.order.application.dto;

import java.util.List;

public class OrderCreateRequest {
    private final Long orderTableId;

    private final List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        validateOrderLineItem(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItem(List<OrderLineItemCreateRequest> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 비어 있을 수 없습니다.");
        }
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
