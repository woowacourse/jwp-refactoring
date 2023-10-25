package kitchenpos.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.dto.response.OrderTableResponse;

public class OrderResponse {

    private Long id;
    private OrderTableResponse orderTable;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(
            Long id,
            OrderTableResponse orderTable,
            String orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItemResponse> orderLineItems
    ) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                OrderTableResponse.from(order.getOrderTable()),
                order.getOrderStatus(),
                order.getOrderedTime(),
                convertToOrderLineItems(order.getOrderLineItems())
        );
    }

    private static List<OrderLineItemResponse> convertToOrderLineItems(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::from)
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
