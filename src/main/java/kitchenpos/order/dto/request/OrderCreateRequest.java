package kitchenpos.order.dto.request;

import java.util.List;
import kitchenpos.order.domain.Order;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final List<OrderLineItemRequest> orderLineItems;

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder() {
        return new Order(orderTableId);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
