package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                         List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order, List<OrderLineItem> orderLineItems) {
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(),
                order.getOrderedTime(), toOrderLineItemResponses(orderLineItems));
    }

    private static List<OrderLineItemResponse> toOrderLineItemResponses(List<OrderLineItem> savedOrderLineItems) {
        return savedOrderLineItems.stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());
    }

    public static OrderResponse from(Order order) {
        return of(order, order.getOrderLineItems());
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
