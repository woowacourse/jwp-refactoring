package kitchenpos.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.vo.OrderLineItems;
import kitchenpos.order.domain.vo.OrderStatus;
import kitchenpos.ordertable.dto.response.OrderTableResponse;

public class OrderResponse {
    private final long id;
    private final OrderTableResponse orderTable;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(
            final long id,
            final OrderTableResponse orderTable,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime,
            final List<OrderLineItemResponse> orderLineItems
    ) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order) {
        final OrderLineItems orderLineItems = order.orderLineItems();
        return new OrderResponse(
                order.id(),
                OrderTableResponse.from(order.orderTable()),
                order.orderStatus(),
                order.orderedTime(),
                parseOrderLineItemResponses(orderLineItems)
        );
    }

    private static List<OrderLineItemResponse> parseOrderLineItemResponses(final OrderLineItems orderLineItems) {
        return orderLineItems.orderLineItems()
                .stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toUnmodifiableList());
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
