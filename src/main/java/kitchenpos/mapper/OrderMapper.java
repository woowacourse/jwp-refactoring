package kitchenpos.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderResponse;

public class OrderMapper {

    private OrderMapper() {
    }

    public static Order toOrder(
            final OrderTable orderTable,
            final OrderStatus status,
            final LocalDateTime orderedTime
            ) {
        return new Order(
                null,
                orderTable,
                status.name(),
                orderedTime,
                null
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
                                orderLineItem.getOrderId(),
                                orderLineItem.getMenuId(),
                                orderLineItem.getQuantity()
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
