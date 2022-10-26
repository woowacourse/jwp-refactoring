package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    private OrderCreateRequest() {
    }

    public Order toOrder() {
        final Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(
                orderLineItems.stream()
                        .map(OrderLineItemRequest::toOrderLineItem)
                        .collect(Collectors.toList())
        );
        return order;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
