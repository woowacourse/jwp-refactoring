package kitchenpos.order.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

public class OrderResponse {

    private Long id;

    private Long orderTableId;

    private String orderStatus;

    private LocalDateTime orderedTime;

    private List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(final Long id,
                         final Long orderTableId,
                         final String orderStatus,
                         final LocalDateTime orderedTime,
                         final List<OrderLineItemResponse> orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order, final OrderLineItems orderLineItems) {
        final List<OrderLineItemResponse> orderLineItemResponses = orderLineItems.getOrderLineItems().stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toUnmodifiableList());

        return new OrderResponse(order.getId(),
            order.getOrderTableId(),
            order.getOrderStatus(),
            order.getOrderedTime(),
            orderLineItemResponses);
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
