package kitchenpos.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderResponse {

    private Long id;
    private Long tableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItemResponses;

    private OrderResponse(Long id, Long tableId, OrderStatus orderStatus, LocalDateTime orderedTime,
        List<OrderLineItemResponse> orderLineItemResponses) {
        this.id = id;
        this.tableId = tableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public static OrderResponse from(Order order, List<OrderLineItem> orderLineItem) {
        List<OrderLineItemResponse> orderLineItemResponses = orderLineItem.stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getTable().getId(),
            order.getOrderStatus(), order.getOrderedTime(), orderLineItemResponses);
    }

    public Long getId() {
        return id;
    }

    public Long getTableId() {
        return tableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItemResponses;
    }
}
