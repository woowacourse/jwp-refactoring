package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.response.OrderLineItemResponse;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
    }

    public static Order mapToOrder(final OrderCreateRequest orderCreateRequest, final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, OrderStatus.valueOf(orderCreateRequest.getOrderStatus()),
                new OrderLineItems(orderLineItems));
    }

    public static OrderStatus mapToOrderStatus(final OrderStatusChangeRequest orderStatusChangeRequest) {
        return OrderStatus.valueOf(orderStatusChangeRequest.getOrderStatus());
    }

    public static OrderResponse mapToResponse(final Order order) {
        final List<OrderLineItemResponse> orderLineItems = order.getOrderLineItems()
                .getValues()
                .stream()
                .map(it -> new OrderLineItemResponse(it.getSeq(), it.getOrder().getId(), it.getMenu().getId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTable().getId(),
                order.getOrderStatus().name(), order.getOrderedTime(), orderLineItems);
    }
}
