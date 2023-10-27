package kitchenpos.order.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;

public class OrderResponse {

    private final long id;
    private final long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(final long id, final long orderTableId, final String orderStatus,
                         final LocalDateTime orderedTime,
                         final List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTable().getId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                OrderLineItemResponse.from(order.getOrderLineItems(), order.getId())
        );
    }

    public static List<OrderResponse> from(final List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public long getOrderTableId() {
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
