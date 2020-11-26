package kitchenpos.order.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderResponse {
    private Long orderId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private Long orderTableId;
    private List<OrderLineItemResponse> orderLineItemResponses;

    public OrderResponse(Long orderId, OrderStatus orderStatus, LocalDateTime orderedTime, Long orderTableId, List<OrderLineItemResponse> orderLineItemResponses) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderTableId = orderTableId;
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public static OrderResponse of(Order order, List<OrderLineItem> orderLineItems) {
        List<OrderLineItemResponse> orderLineItemResponses = new ArrayList<>();
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItemResponses.add(OrderLineItemResponse.of(orderLineItem));
        }
        return new OrderResponse(order.getId(), order.getOrderStatus(), order.getOrderedTime(), order.getOrderTable().getId(), orderLineItemResponses);
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItemResponses;
    }
}
