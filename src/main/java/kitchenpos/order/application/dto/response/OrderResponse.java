package kitchenpos.order.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;

public class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(final Long id, final Long orderTableId, final String orderStatus,
                          final LocalDateTime orderedTime,
                          final List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                OrderLineItemResponse.from(order.getOrderLineItemsValue(), order.getId())
        );
    }

    public static List<OrderResponse> from(List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
