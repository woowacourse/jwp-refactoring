package kitchenpos.application.dto;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.entity.Order;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    private OrderResponse() {
    }

    public OrderResponse(Long id, Long orderTableId, String orderStatus,
            LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
                order.getOrderedTime(), order.getOrderLineItems());
    }

    public static List<OrderResponse> listOf(List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::of)
                .collect(toList());
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
