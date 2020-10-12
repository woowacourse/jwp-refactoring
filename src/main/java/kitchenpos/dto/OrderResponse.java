package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private TableResponse orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public static List<OrderResponse> listFrom(List<Order> orders) {
        return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    public static OrderResponse from(Order order) {
        TableResponse table = TableResponse.from(order.getOrderTable());
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public Long getId() {
        return id;
    }

    public TableResponse getOrderTable() {
        return orderTable;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
