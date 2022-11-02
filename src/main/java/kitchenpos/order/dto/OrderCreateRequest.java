package kitchenpos.order.dto;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;

public class OrderCreateRequest {
    private Long orderTableId;
    private List<OrderLineItem> orderLineItems;

    private OrderCreateRequest() {
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
