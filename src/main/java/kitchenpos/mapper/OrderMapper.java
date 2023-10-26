package kitchenpos.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderResponse;

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
                                orderLineItem.getOrderId()
                                        .orElseThrow(() -> new IllegalArgumentException("orderId 가 NULL 입니다")),
                                orderLineItem.getMenuId()
                                        .orElseThrow(() -> new IllegalArgumentException("menuId 가 NULL 입니다")),
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
