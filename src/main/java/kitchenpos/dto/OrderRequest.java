package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItem> orderLineItems;

    public OrderRequest(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity() {
        return new Order(orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
