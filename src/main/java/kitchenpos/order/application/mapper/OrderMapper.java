package kitchenpos.order.application.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;

public class OrderMapper {

    private OrderMapper() {
    }

    public static Order toOrder(
            final OrderTable orderTable,
            final LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems) {
        return new Order(
                orderTable,
                orderedTime,
                orderLineItems
        );
    }

    public static OrderResponse toOrderResponse(final Order order) {
        final List<OrderLineItemResponse> orderLineItemResponse = toOrderLineItemResponse(order.getOrderLineItems());

        return new OrderResponse(
                order.getId(),
                order.getOrderTableId().orElse(null),
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
