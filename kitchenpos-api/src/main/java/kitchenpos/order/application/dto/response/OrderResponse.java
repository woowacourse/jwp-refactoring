package kitchenpos.order.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;

public record OrderResponse(Long id,
                            Long orderTableId,
                            String orderStatus,
                            LocalDateTime orderedTime,
                            List<OrderLineItemResponse> orderLineItems) {

    public static OrderResponse from(final Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems().stream()
                .map(OrderLineItemResponse::from)
                .toList();

        return new OrderResponse(order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                orderLineItemResponses);
    }
}
