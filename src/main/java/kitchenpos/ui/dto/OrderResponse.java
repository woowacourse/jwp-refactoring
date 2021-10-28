package kitchenpos.ui.dto;

import kitchenpos.domain.Orders;
import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse() {
    }

    private OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Orders orders, List<OrderLineItem> orderLineItems) {
        return new OrderResponse(
                orders.getId(),
                orders.getOrderTableId(),
                orders.getOrderStatus(),
                orders.getOrderedTime(),
                OrderLineItemResponse.from(orderLineItems)
        );
    }

    public static List<OrderResponse> from(Map<Orders, List<OrderLineItem>> results) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        results.forEach((key, value) -> orderResponses.add(
                OrderResponse.of(key, value)
        ));
        return orderResponses;
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
