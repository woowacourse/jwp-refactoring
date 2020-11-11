package kitchenpos.dto.order;

import kitchenpos.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse() {
    }

    public OrderResponse(Long id) {
        this.id = id;
    }

    public OrderResponse(Long id, Long orderTableId, String orderStatus,
                         LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(Order order, List<OrderLineItemResponse> orderLineItemResponses) {
        return new OrderResponse(order.getId(), order.getContainOrderTableId(), order.getOrderStatus().name(),
                order.getOrderedTime(), orderLineItemResponses);
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
