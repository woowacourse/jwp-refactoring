package kitchenpos.ui.dto;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Orders;

import java.time.LocalDateTime;
import java.util.List;

public class OrdersResponse {

    private Long id;
    private Long orderTableId;
    private OrderStatus status;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItemResponses;

    public OrdersResponse() {
    }

    public OrdersResponse(Long id, Long orderTableId, OrderStatus status,
                          LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItemResponses) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.status = status;
        this.orderedTime = orderedTime;
        this.orderLineItemResponses = orderLineItemResponses;
    }

    public static OrdersResponse of(Orders newOrder, List<OrderLineItemResponse> orderLineItemResponses) {
        return new OrdersResponse(newOrder.getId(), newOrder.getOrderTableId(),
                newOrder.getOrderStatus(), newOrder.getOrderedTime(), orderLineItemResponses);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemResponse> getOrderLineItemResponses() {
        return orderLineItemResponses;
    }
}
