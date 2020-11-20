package kitchenpos.dto.order;

import kitchenpos.domain.Order;

import java.time.LocalDateTime;

public class OrderFindAllResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private OrderLineItemFindAllResponses orderLineItemFindAllResponses;

    protected OrderFindAllResponse() {
    }

    public OrderFindAllResponse(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, OrderLineItemFindAllResponses orderLineItemFindAllResponses) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemFindAllResponses = orderLineItemFindAllResponses;
    }

    public OrderFindAllResponse(Order order) {
        this(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(), OrderLineItemFindAllResponses.from(order.getOrderLineItems()));
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

    public OrderLineItemFindAllResponses getOrderLineItemFindAllResponses() {
        return orderLineItemFindAllResponses;
    }
}
