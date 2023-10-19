package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
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
}
