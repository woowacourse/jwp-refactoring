package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.OrderLineItem;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItem> orderLineItems;

    public OrderRequest(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
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
