package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderRequest() {
    }

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toEntity(final List<OrderLineItem> orderLineItems) {
        return new Order(OrderStatus.COOKING, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
