package kitchenpos.dto;

import kitchenpos.menu.domain.MenuHistory;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;

public class OrderHistoryResponse {

    private long id;
    private OrderTableResponse orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemHistoryResponse> orderLineItems;

    public OrderHistoryResponse(final long id,
                                final OrderTableResponse orderTable,
                                final OrderStatus orderStatus,
                                final LocalDateTime orderedTime,
                                final List<OrderLineItemHistoryResponse> orderLineItems
    ) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderHistoryResponse from(final Order order, final OrderTable orderTable, final List<MenuHistory> menuHistories) {
        return new OrderHistoryResponse(
                order.getId(),
                OrderTableResponse.from(orderTable),
                order.getOrderStatus(),
                order.getOrderedTime(),
                OrderLineItemHistoryResponse.from(order.getOrderLineItems().getOrderLineItems(), menuHistories)
        );
    }

    public long getId() {
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

    public List<OrderLineItemHistoryResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
