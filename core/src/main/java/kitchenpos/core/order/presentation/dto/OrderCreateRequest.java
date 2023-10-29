package kitchenpos.core.order.presentation.dto;

import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemCreateRequest> orderLineItems) {
        validate(orderLineItems);
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    private static void validate(final List<OrderLineItemCreateRequest> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문할 항목은 최소 1개 이상이어야 합니다.");
        }
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
