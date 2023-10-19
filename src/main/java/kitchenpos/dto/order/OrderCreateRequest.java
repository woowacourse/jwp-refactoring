package kitchenpos.dto.order;

import java.util.List;
import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final List<OrderLineItemRequest> orderLineItems;

    private OrderCreateRequest(final Long orderTableId,
                               OrderStatus orderStatus, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static OrderCreateRequest of(final Long orderTableId,
                                        final OrderStatus orderStatus,
                                        final List<OrderLineItemRequest> orderLineItems) {
        return new OrderCreateRequest(orderTableId, orderStatus, orderLineItems);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
