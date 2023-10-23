package kitchenpos.dto.order;

import java.util.List;
import kitchenpos.domain.OrderStatus;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderCreateRequest(
            final Long orderTableId,
            final OrderStatus orderStatus,
            final List<OrderLineItemRequest> orderLineItems
    ) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
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
