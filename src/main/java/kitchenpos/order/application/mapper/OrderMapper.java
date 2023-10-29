package kitchenpos.order.application.mapper;

import kitchenpos.order.application.dto.response.OrderLineItemResponse;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponse mapToOrderResponseBy(final Order order, final List<OrderLineItem> orderLineItems) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(), order.getOrderedTime(),
                orderLineItems.stream()
                        .map(OrderMapper::mapToOrderLineItemResponseBy)
                        .collect(Collectors.toList()));
    }

    private static OrderLineItemResponse mapToOrderLineItemResponseBy(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrder().getId(),
                orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }
}
