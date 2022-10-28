package kitchenpos.order.ui.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse() {
    }

    public OrderResponse(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                         final List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTableId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                toOrderLineItemResponses(order.getOrderLineItems())
        );
    }

    public static List<OrderLineItemResponse> toOrderLineItemResponses(final List<OrderLineItem> items) {
        return items.stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
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
