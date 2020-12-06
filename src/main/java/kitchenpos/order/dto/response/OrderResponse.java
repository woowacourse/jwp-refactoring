package kitchenpos.order.dto.response;

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
    private List<OrderLineItemResponse> orderLineItemResponses;

    public OrderResponse(Long orderId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItemResponses) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public static OrderResponse of(Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = new ArrayList<>();
        for (OrderLineItem orderLineItem : order.showOrderLineItems()) {
            orderLineItemResponses.add(OrderLineItemResponse.of(orderLineItem));
        }
        return new OrderResponse(order.getId(), order.getOrderStatus(), order.getOrderedTime(), orderLineItemResponses);
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

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItemResponses;
    }
}
