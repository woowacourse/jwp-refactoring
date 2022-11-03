package kitchenpos.order.application.dto.request;

import kitchenpos.order.domain.OrderStatus;

import java.util.List;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final String orderStatus;
    private final List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final String orderStatus,
                              final List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public OrderCreateRequest(final Long orderTableId,
                              final List<OrderLineItemCreateRequest> orderLineItems) {
        this(orderTableId, OrderStatus.COOKING.name(), orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
