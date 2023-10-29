package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.response.OrderLineItemResponse;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderStatus mapToOrderStatus(final OrderStatusChangeRequest orderStatusChangeRequest) {
        return OrderStatus.valueOf(orderStatusChangeRequest.getOrderStatus());
    }

    public static OrderResponse mapToResponse(final Order order) {
        final List<OrderLineItemResponse> orderLineItems = order.getOrderLineItems()
                .getValues()
                .stream()
                .map(it -> new OrderLineItemResponse(it.getSeq(), it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTableId(),
                order.getOrderStatus().name(), order.getOrderedTime(), orderLineItems);
    }
}
