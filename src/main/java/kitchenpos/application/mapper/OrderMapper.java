package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.response.OrderLineItemResponse;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
    }

    public static Order mapToOrder(final OrderCreateRequest orderCreateRequest) {
        final List<OrderLineItem> orderLineItems = orderCreateRequest.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new Order(orderCreateRequest.getOrderTableId(), orderCreateRequest.getOrderStatus(),
                orderCreateRequest.getOrderedTime(), orderLineItems);
    }

    public static OrderStatus mapToOrderStatus(final OrderStatusChangeRequest orderStatusChangeRequest) {
        return OrderStatus.valueOf(orderStatusChangeRequest.getOrderStatus());
    }

    public static OrderResponse mapToResponse(final Order order) {
        final List<OrderLineItemResponse> orderLineItems = order.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItemResponse(it.getSeq(), it.getOrderId(), it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTableId(),
                order.getOrderStatus(), order.getOrderedTime(), orderLineItems);
    }
}
