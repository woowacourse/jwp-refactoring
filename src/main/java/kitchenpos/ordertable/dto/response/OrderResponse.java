package kitchenpos.ordertable.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.Order;

public class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOrderTableId(),
            order.getOrderStatus(),
            order.getOrderedTime(),
            OrderLineItemResponse.of(order.getOrderLineItems())
        );
    }

    public static List<OrderResponse> of(List<Order> orders) {
        return orders.stream()
            .map(OrderResponse::of)
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
