package kitchenpos.ui.dto;

import kitchenpos.domain.order.Order;
import kitchenpos.ui.OrderTableResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(Long id, OrderTableResponse orderTable, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order) {
        return new OrderResponse(
                order.getId(),
                OrderTableResponse.of(order.getOrderTable()),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                OrderLineItemResponse.toList(order.getOrderLineItems())
        );
    }

    public static List<OrderResponse> toList(List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTable() {
        return orderTable;
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
