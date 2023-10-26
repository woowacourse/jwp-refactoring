package kitchenpos.order.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemsResponse {

    private final List<OrderLineItemResponse> orderLineItems;

    private OrderLineItemsResponse(final List<OrderLineItemResponse> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItemsResponse from(final List<OrderLineItem> orderLineItems) {
        List<OrderLineItemResponse> orderLineItemResponses = orderLineItems.stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toUnmodifiableList());
        return new OrderLineItemsResponse(orderLineItemResponses);
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
