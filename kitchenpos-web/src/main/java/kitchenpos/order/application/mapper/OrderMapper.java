package kitchenpos.order.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderResponse;

public class OrderMapper {

    private OrderMapper() {
    }

    public static Order toOrder(
            final Long orderTableId,
            final List<OrderLineItem> orderLineItems) {
        return new Order(
                orderTableId,
                orderLineItems
        );
    }

    public static OrderResponse toOrderResponse(final Order order) {
        final List<OrderLineItemResponse> orderLineItemResponse = toOrderLineItemResponse(order.getOrderLineItems());

        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                orderLineItemResponse
        );
    }

    private static List<OrderLineItemResponse> toOrderLineItemResponse(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemResponse(
                                orderLineItem.getSeq(),
                                orderLineItem.getMenuId(),
                                orderLineItem.getQuantity().getValue()
                        )
                )
                .collect(Collectors.toList());
    }

    public static List<OrderResponse> toOrderResponses(final List<Order> orders) {
        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }
}
