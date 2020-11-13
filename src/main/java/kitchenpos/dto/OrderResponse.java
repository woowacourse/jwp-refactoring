package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public static List<OrderResponse> listFrom(final List<Order> orders) {
        return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    public static OrderResponse from(final Order order) {
        OrderTable orderTable = order.getOrderTable();
        List<OrderLineItemResponse> orderLineItems = OrderLineItemResponse
            .listFrom(order.getOrderLineItems());

        return OrderResponse.builder()
            .id(order.getId())
            .orderTableId(orderTable.getId())
            .orderStatus(order.getOrderStatus())
            .orderedTime(order.getOrderedTime())
            .orderLineItems(orderLineItems)
            .build();
    }
}
