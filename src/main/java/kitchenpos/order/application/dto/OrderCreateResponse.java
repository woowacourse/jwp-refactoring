package kitchenpos.order.application.dto;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;

public class OrderCreateResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemCreateResponse> orderLineItems;

    public OrderCreateResponse(final Long id, final Long orderTableId, final String orderStatus,
                               final LocalDateTime orderedTime,
                               final List<OrderLineItemCreateResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderCreateResponse of(final Order order) {
        final List<OrderLineItemCreateResponse> orderLineItemCreateResponses = order.getOrderLineItems().stream()
                .map(OrderLineItemCreateResponse::of)
                .collect(toList());
        return new OrderCreateResponse(
                order.getId(),
                order.getOrderTable().getId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                orderLineItemCreateResponses
        );
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

    public List<OrderLineItemCreateResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
