package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItemResponses;

    private OrderResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
        List<OrderLineItemResponse> orderLineItemResponses) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public static OrderResponse of(Order order) {
        Long id = order.getId();
        Long orderTableId = order.getOrderTable().getId();
        String orderStatus = order.getOrderStatus();
        LocalDateTime orderedTime = order.getOrderedTime();
        List<OrderLineItemResponse> orderLineItemResponses = OrderLineItemResponse
            .toResponseList(order.getOrderLineItems());

        return new OrderResponse(id, orderTableId, orderStatus, orderedTime,
            orderLineItemResponses);
    }

    public static OrderResponse of(Order order,
        List<OrderLineItemResponse> orderLineItemResponses) {
        Long id = order.getId();
        Long orderTableId = order.getOrderTable().getId();
        String orderStatus = order.getOrderStatus();
        LocalDateTime orderedTime = order.getOrderedTime();

        return new OrderResponse(id, orderTableId, orderStatus, orderedTime,
            orderLineItemResponses);
    }

    public static List<OrderResponse> toResponseList(List<Order> orders) {
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

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItemResponses;
    }
}
