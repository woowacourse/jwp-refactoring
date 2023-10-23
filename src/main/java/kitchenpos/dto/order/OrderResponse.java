package kitchenpos.dto.order;

import java.time.LocalDateTime;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.table.OrderTableResponse;

public class OrderResponse {

    private final Long id;
    private final OrderTableResponse orderTable;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final OrderLineItemsResponse orderLineItems;

    private OrderResponse(final Long id, final OrderTableResponse orderTable, final OrderStatus orderStatus,
                          final LocalDateTime orderedTime,
                          final OrderLineItemsResponse orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(order.getId(), OrderTableResponse.from(order.getOrderTable()), order.getOrderStatus(),
                order.getOrderedTime(), OrderLineItemsResponse.from(order.getOrderLineItems()));
    }

    public Long getId() {
        return id;
    }

    public OrderTableResponse getOrderTable() {
        return orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItemsResponse getOrderLineItems() {
        return orderLineItems;
    }
}
