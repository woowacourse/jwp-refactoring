package kitchenpos.application.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public static OrderResponse from(Order order) {
        final OrderResponse orderResponse = new OrderResponse();
        orderResponse.id = order.getId();
        orderResponse.orderTableId = order.getOrderTable().getId();
        orderResponse.orderStatus = order.getOrderStatus().name();
        orderResponse.orderedTime = order.getOrderedTime();
        orderResponse.orderLineItems = OrderLineItemResponse.fromList(order.getOrderLineItems());
        return orderResponse;
    }

    public static List<OrderResponse> fromList(List<Order> orders) {
        return orders.stream()
            .map(OrderResponse::from)
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
