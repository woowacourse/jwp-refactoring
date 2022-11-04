package kitchenpos.order.application.dto;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderRequest {

    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItemsRequest;

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItemsRequest) {
        this.orderTableId = orderTableId;
        this.orderLineItemsRequest = orderLineItemsRequest;
    }

    public Order toOrder(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItemsRequest() {
        return orderLineItemsRequest;
    }
}
