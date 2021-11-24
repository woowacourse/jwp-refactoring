package kitchenpos.order.ui.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {
    private Long id;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(Long id, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                orderLineItemResponse(order)
        );
    }

    private static List<OrderLineItemResponse> orderLineItemResponse(Order order) {
        return order.getOrderLineItemLists().stream()
                    .map(OrderLineItemResponse::from)
                    .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
