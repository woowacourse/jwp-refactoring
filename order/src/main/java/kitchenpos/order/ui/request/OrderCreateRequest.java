package kitchenpos.order.ui.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public static OrderCreateRequest create(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.orderTableId = orderTableId;
        orderCreateRequest.orderLineItems = orderLineItems;
        return orderCreateRequest;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }

    public Order toEntity() {
        final List<OrderLineItem> orderLineItems = this.orderLineItems.stream()
            .map(OrderLineItemRequest::toEntity)
            .collect(Collectors.toList());
        return Order.create(orderTableId, orderLineItems);
    }
}
