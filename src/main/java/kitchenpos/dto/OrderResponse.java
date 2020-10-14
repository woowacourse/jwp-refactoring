package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public static List<OrderResponse> listFrom(List<Order> orders) {
        return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    public static OrderResponse from(Order order) {
        OrderTableResponse table = OrderTableResponse.from(order.getOrderTable());
        List<OrderLineItemResponse> orderLineItems = OrderLineItemResponse
            .listFrom(order.getOrderLineItems());

        return OrderResponse.builder()
            .id(order.getId())
            .orderTable(table)
            .orderStatus(order.getOrderStatus())
            .orderedTime(order.getOrderedTime())
            .orderLineItems(orderLineItems)
            .build();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTable() {
        return orderTable;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
