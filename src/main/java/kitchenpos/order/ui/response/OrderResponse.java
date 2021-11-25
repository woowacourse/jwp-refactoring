package kitchenpos.order.ui.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;

public class OrderResponse {

    private final Long id;
    private final Long orderTable;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(
        Long id,
        Long orderTable,
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
            order.getOrderTableId(),
            order.getOrderStatus(),
            order.getOrderedTime(),
            OrderLineItemResponse.of(order.getOrderLineItems())
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTable() {
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
