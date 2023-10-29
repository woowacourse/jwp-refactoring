package kitchenpos.order.application.response;

import kitchenpos.order.domain.MenuHistory;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderHistoryResponse {

    private long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemHistoryResponse> orderLineItems;

    public OrderHistoryResponse(final long id,
                                final Long orderTableId,
                                final OrderStatus orderStatus,
                                final LocalDateTime orderedTime,
                                final List<OrderLineItemHistoryResponse> orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderHistoryResponse from(final Order order, final List<MenuHistory> menuHistories) {
        return new OrderHistoryResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                OrderLineItemHistoryResponse.from(order.getOrderLineItems().getOrderLineItems(), menuHistories)
        );
    }

    public long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
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
