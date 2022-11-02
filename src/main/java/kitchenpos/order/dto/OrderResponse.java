package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;

public class OrderResponse {

    private final Long orderTableId;
    private final List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(Long orderTableId, List<OrderLineItemResponse> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems().stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toList());

        return new OrderResponse(order.getOrderTable().getId(), orderLineItemResponses);
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
