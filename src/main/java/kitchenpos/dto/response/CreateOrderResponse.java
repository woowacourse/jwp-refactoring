package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

public class CreateOrderResponse {
    private Long id;
    private OrderTable orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    public CreateOrderResponse(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static CreateOrderResponse from(Order order) {
        return new CreateOrderResponse(
                order.getId(),
                order.getOrderTable(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                orderLineItemResponse(order)
        );
    }

    private static List<OrderLineItemResponse> orderLineItemResponse(Order order) {
        return order.getOrderLineItems().stream()
                    .map(OrderLineItemResponse::from)
                    .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
