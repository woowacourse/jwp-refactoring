package kitchenpos.order.ui.response;

import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.ui.response.OrderTableResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderResponse {
    private final Long id;
    private final OrderTableResponse orderTable;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(final Long id,
                         final OrderTableResponse orderTable,
                         final String orderStatus,
                         final LocalDateTime orderedTime,
                         final List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order savedOrder) {
        List<OrderLineItemResponse> orderLineItemResponses = savedOrder.getOrderLineItems().stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(
                savedOrder.getId(),
                OrderTableResponse.from(savedOrder.getOrderTable()),
                savedOrder.getOrderStatus().name(),
                savedOrder.getOrderedTime(),
                orderLineItemResponses
        );
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderResponse that = (OrderResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(orderTable, that.orderTable)
                && Objects.equals(orderStatus, that.orderStatus)
                && Objects.equals(orderedTime, that.orderedTime)
                && Objects.equals(orderLineItems, that.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTable, orderStatus, orderedTime, orderLineItems);
    }
}
