package kitchenpos.dto.order;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItems;

import java.time.LocalDateTime;

public class OrderCreateResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private OrderLineItemCreateResponses orderLineItemCreateResponses;

    protected OrderCreateResponse() {
    }

    public OrderCreateResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, OrderLineItemCreateResponses orderLineItemCreateResponses) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemCreateResponses = orderLineItemCreateResponses;
    }

    public OrderCreateResponse(Order order) {
        this(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(), OrderLineItemCreateResponses.from(order.getOrderLineItems()));
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

    public OrderLineItemCreateResponses getOrderLineItemCreateResponses() {
        return orderLineItemCreateResponses;
    }
}
