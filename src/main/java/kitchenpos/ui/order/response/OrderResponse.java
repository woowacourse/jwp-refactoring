package kitchenpos.ui.order.response;

import kitchenpos.order.Order;
import kitchenpos.order.OrderLineItem;

import java.util.List;

import static java.util.stream.Collectors.*;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private String orderedTime;
    private List<OrderLineItem> orderLineItems;

    public OrderResponse() {
    }

    public OrderResponse(Long id, Long orderTableId, String orderStatus, String orderedTime, List<OrderLineItem> orderLineItems) {
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
                order.getOrderStatus(),
                order.getOrderedTime().toString(),
                order.getOrderLineItems()
        );
    }

    public static List<OrderResponse> of(List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::from)
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

    public String getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
