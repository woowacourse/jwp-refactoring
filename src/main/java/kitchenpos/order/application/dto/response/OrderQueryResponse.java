package kitchenpos.order.application.dto.response;

import kitchenpos.order.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderQueryResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemQueryResponse> orderLineItems;

    public OrderQueryResponse(final Long id, final Long orderTableId, final String orderStatus,
                              final LocalDateTime orderedTime,
                              final List<OrderLineItemQueryResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public OrderQueryResponse() {
    }

    public static OrderQueryResponse from(final Order order) {
        final List<OrderLineItemQueryResponse> orderLineItemQueryResponses
                = order.getOrderLineItems().getOrderLineItems()
                .stream()
                .map(orderLineItem -> OrderLineItemQueryResponse.of(order.getId(), orderLineItem))
                .collect(Collectors.toList());

        return new OrderQueryResponse(order.getId(), order.getOrderTableId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(), orderLineItemQueryResponses);
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

    public List<OrderLineItemQueryResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
