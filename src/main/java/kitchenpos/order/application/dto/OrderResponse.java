package kitchenpos.order.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderResponse {
    private final Long id;
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    public static OrderResponse from(final Order order) {
        final List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems()
            .stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus()
            , order.getOrderedTime(), orderLineItemResponses);
    }
}
