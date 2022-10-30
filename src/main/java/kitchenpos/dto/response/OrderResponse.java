package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse() {
    }

    public OrderResponse(final Order order) {
        this(order.getId(), order.getOrderTable().getId(), order.getOrderStatus(), order.getOrderedTime(),
                getOrderLineItemResponses(order));
    }

    private static List<OrderLineItemResponse> getOrderLineItemResponses(final Order order) {
        return order.getOrderLineItems().stream().map(OrderLineItemResponse::new).collect(Collectors.toList());
    }

    public OrderResponse(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                         final LocalDateTime orderedTime,
                         final List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
