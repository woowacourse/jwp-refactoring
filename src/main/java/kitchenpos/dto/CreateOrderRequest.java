package kitchenpos.dto;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

import java.util.List;
import java.util.stream.Collectors;

public class CreateOrderRequest {

    private Long orderTableId;
    private List<CreateOrderLineItemRequest> orderLineItems;

    public CreateOrderRequest() {
    }

    public CreateOrderRequest(final Long orderTableId, final List<CreateOrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderLineItems toOrderLineItems() {
        final List<OrderLineItem> orderLineItemEntities = orderLineItems.stream()
                                                          .map(CreateOrderLineItemRequest::toOrderLineItem)
                                                          .collect(Collectors.toList());
        return new OrderLineItems(orderLineItemEntities);
    }
}
